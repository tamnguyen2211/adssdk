package com.drowsyatmidnight.haint.androidadssdk;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

final class PlayerManager implements AdsMediaSource.MediaSourceFactory {

    private ImaAdsLoader adsLoader;
    private DataSource.Factory manifestDataSourceFactory;
    private DataSource.Factory mediaDataSourceFactory;

    public SimpleExoPlayer player;
    public long contentPosition;
    public String adsResponse;
    private Context context;
    private AdsResponseListener adsResponseListener;

    public void setAdsResponse(String adsResponse) {
        this.adsResponse = adsResponse;
    }

    public PlayerManager(Context context, AdsResponseListener adsResponseListener) {
        this.context = context;
        this.adsResponseListener = adsResponseListener;
        getAds();
    }

    private void getAds(){
        String adTag = context.getString(R.string.ad_tag_url);
        OkHttpClient client = new OkHttpClient();
        Request requestAds = new Request.Builder()
                .url(adTag)
                .build();
        client.newCall(requestAds).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                adsResponseListener.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                adsResponseListener.onSuccess(response.body().string());
            }
        });
    }

    public void init(PlayerView playerView) {
        adsLoader = new ImaAdsLoader(context, null, null,
                adsResponse, ImaAdsLoader.TIMEOUT_UNSET, ImaAdsLoader.TIMEOUT_UNSET, null);

        manifestDataSourceFactory =
                new DefaultDataSourceFactory(
                        context, Util.getUserAgent(context, context.getString(R.string.app_name)));
        mediaDataSourceFactory =
                new DefaultDataSourceFactory(
                        context,
                        Util.getUserAgent(context, context.getString(R.string.app_name)),
                        new DefaultBandwidthMeter());
        // Create a default track selector.
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // Create a player instance.
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

        // Bind the player to the view.
        playerView.setPlayer(player);

        // This is the MediaSource representing the content media (i.e. not the ad).
        String contentUrl = context.getString(R.string.content_vod);
        MediaSource contentMediaSource = buildMediaSource(Uri.parse(contentUrl));

        // Compose the content media source into a new AdsMediaSource with both ads and content.
        MediaSource mediaSourceWithAds =
                new AdsMediaSource(
                        contentMediaSource,
                        /* adMediaSourceFactory= */ this,
                        adsLoader,
                        playerView.getOverlayFrameLayout());

        // Prepare the player with the source.
        player.seekTo(contentPosition);
        player.prepare(mediaSourceWithAds);
        player.setPlayWhenReady(true);
    }

    public void reset() {
        if (player != null) {
            contentPosition = player.getContentPosition();
            player.release();
            player = null;
        }
    }

    public void release() {
        if (player != null) {
            player.release();
            player = null;
        }
        adsLoader.release();
    }

    // AdsMediaSource.MediaSourceFactory implementation.

    @Override
    public MediaSource createMediaSource(Uri uri) {
        return buildMediaSource(uri);
    }

    @Override
    public int[] getSupportedTypes() {
        // IMA does not support Smooth Streaming ads.
        return new int[]{C.TYPE_DASH, C.TYPE_HLS, C.TYPE_OTHER};
    }

    // Internal methods.

    private MediaSource buildMediaSource(Uri uri) {
        @C.ContentType int type = Util.inferContentType(uri);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                        manifestDataSourceFactory)
                        .createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory), manifestDataSourceFactory)
                        .createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uri);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }
}
