package com.drowsyatmidnight.haint.androidadssdk;

import android.content.Context;
import android.net.Uri;
import android.widget.Button;
import android.widget.FrameLayout;

import com.drowsyatmidnight.haint.android_fplay_ads_sdk.AdsController;
import com.drowsyatmidnight.haint.android_fplay_ads_sdk.AdsListener;
import com.drowsyatmidnight.haint.android_vpaid_sdk.VpaidView;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
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

final class PlayerManager implements AdsMediaSource.MediaSourceFactory, AdsListener.VideoProgress, AdsListener.PlayerStaus {

    private DataSource.Factory manifestDataSourceFactory;
    private DataSource.Factory mediaDataSourceFactory;

    public SimpleExoPlayer player;
    public long contentPosition;
    private Context context;
    private AdsResponseListener adsResponseListener;
    private FrameLayout adsView;
    private static final String TAG = PlayerManager.class.getSimpleName();
    private AdsController adsController;
    private VpaidView vpaidView;

    public PlayerManager(Context context, AdsResponseListener adsResponseListener) {
        this.context = context;
        this.adsResponseListener = adsResponseListener;
    }

    public PlayerManager(Context context) {
        this.context = context;
        this.adsResponseListener = null;
    }

    public void init(PlayerView playerView, String contentUrl, String channelId) {
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
        adsView = playerView.getOverlayFrameLayout();
        adsController = AdsController.init(context, this, this);
        adsController.startAdsLiveTV("", 306, "quang-tri", "android", adsView);
        // This is the MediaSource representing the content media (i.e. not the ad).
        MediaSource contentMediaSource = buildMediaSource(Uri.parse(contentUrl));
        // Prepare the player with the source.
        player.seekTo(contentPosition);
        player.prepare(contentMediaSource);
        player.setPlayWhenReady(true);
    }

    public void init(PlayerView playerView, VpaidView vpaidView, String contentUrl, Button skipButton) {
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
        adsView = playerView.getOverlayFrameLayout();
        this.vpaidView = vpaidView;
        adsController = AdsController.init(context, this, this);
        adsController.startAdsVod("", 306, "", adsView, vpaidView, skipButton);

        // This is the MediaSource representing the content media (i.e. not the ad).
        MediaSource contentMediaSource = buildMediaSource(Uri.parse(contentUrl));
        // Prepare the player with the source.
        player.seekTo(contentPosition);
        player.prepare(contentMediaSource);
        player.setPlayWhenReady(true);
        player.addListener(new Player.DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    adsController.onCompleted();
                }
            }
        });
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

    @Override
    public long setVideoDuaration() {
        if (player != null) {
            return player.getDuration();
        }
        return 0;
    }

    @Override
    public long setVideoCurrentPosition() {
        if (player != null) {
            return player.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void hiddenPlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }

    @Override
    public void showPlayer() {
        if (player != null) {
            player.setPlayWhenReady(true);
        }
    }
}
