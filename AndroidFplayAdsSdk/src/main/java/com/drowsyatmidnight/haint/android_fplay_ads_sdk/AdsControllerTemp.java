//package com.drowsyatmidnight.haint.android_fplay_ads_sdk;
//
//import android.content.Context;
//import android.media.AudioManager;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.drowsyatmidnight.haint.android_vpaid_sdk.VpaidView;
//import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
//import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
//import com.google.ads.interactivemedia.v3.api.AdEvent;
//import com.google.ads.interactivemedia.v3.api.AdsLoader;
//import com.google.ads.interactivemedia.v3.api.AdsController;
//import com.google.ads.interactivemedia.v3.api.AdsManagerLoadedEvent;
//import com.google.ads.interactivemedia.v3.api.AdsRenderingSettings;
//import com.google.ads.interactivemedia.v3.api.AdsRequest;
//import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
//import com.google.ads.interactivemedia.v3.api.ImaSdkSettings;
//import com.google.ads.interactivemedia.v3.api.player.ContentProgressProvider;
//import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;
//import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
//public class AdsController implements AdsListener.AdsStatus, AdsListener.VideoStatus {
//
//    private AdDisplayContainer mAdDisplayContainer;
//    private AdsLoader mAdsLoader;
//    private AdsController mAdsManager;
//    private ImaSdkFactory mSdkFactory;
//    private boolean mIsAdPlaying;
//    private double mPlayAdsAfterTime = -1;
//    private Context context;
//    private AdsListener.VideoProgress videoProgress;
//    private AdsListener.PlayerStaus playerStaus;
//    private OkHttpClient client;
//    private String vastResponse;
//    private ViewGroup adsView;
//    private VideoAdPlayer mVideoAdPlayer;
//    private final List<VideoAdPlayer.VideoAdPlayerCallback> mAdCallbacks =
//            new ArrayList<>(1);
//
//    private class AdsLoadedListener implements AdsLoader.AdsLoadedListener {
//
//        @Override
//        public void onAdsManagerLoaded(AdsManagerLoadedEvent adsManagerLoadedEvent) {
//            mAdsManager = adsManagerLoadedEvent.getAdsManager();
//            mAdsManager.addAdErrorListener(new AdErrorEvent.AdErrorListener() {
//
//                @Override
//                public void onAdError(AdErrorEvent adErrorEvent) {
////                    log("Ad Error: " + adErrorEvent.getError().getMessage());
//                    resumeContent();
//                }
//            });
//            mAdsManager.addAdEventListener(new AdEvent.AdEventListener() {
//                /**
//                 * Responds to AdEvents.
//                 */
//                @Override
//                public void onAdEvent(AdEvent adEvent) {
////                    log("Event: " + adEvent.getType());
//                    switch (adEvent.getType()) {
//                        case LOADED:
//                            mAdsManager.start();
//                            break;
//                        case CONTENT_PAUSE_REQUESTED:
//                            pauseContent();
//                            break;
//                        case CONTENT_RESUME_REQUESTED:
//                            resumeContent();
//                            break;
//                        case PAUSED:
//                            mIsAdPlaying = false;
//                            break;
//                        case RESUMED:
//                            mIsAdPlaying = true;
//                            break;
//                        case ALL_ADS_COMPLETED:
//                            if (mAdsManager != null) {
//                                mAdsManager.destroy();
//                                mAdsManager = null;
//                            }
//                            break;
//                        default:
//                            break;
//                    }
//                }
//            });
//            AdsRenderingSettings adsRenderingSettings =
//                    ImaSdkFactory.getInstance().createAdsRenderingSettings();
//            adsRenderingSettings.setPlayAdsAfterTime(mPlayAdsAfterTime);
//            mAdsManager.init(adsRenderingSettings);
//        }
//    }
//
//    public AdsController() {
//    }
//
//    public AdsController(Context context, AdsListener.VideoProgress videoProgress, AdsListener.PlayerStaus playerStaus) {
//        this.context = context;
//        this.videoProgress = videoProgress;
//        this.playerStaus = playerStaus;
//        client = new OkHttpClient();
//        ImaSdkSettings imaSdkSettings = ImaSdkFactory.getInstance().createImaSdkSettings();
//        imaSdkSettings.setLanguage("vn");
//        mSdkFactory = ImaSdkFactory.getInstance();
//        mAdsLoader = mSdkFactory.createAdsLoader(context, imaSdkSettings);
//        mAdsLoader.addAdErrorListener(new AdErrorEvent.AdErrorListener() {
//            @Override
//            public void onAdError(AdErrorEvent adErrorEvent) {
////                log("Ad Error: " + adErrorEvent.getError().getMessage());
//                resumeContent();
//            }
//        });
//        mAdsLoader.addAdsLoadedListener(new AdsController.AdsLoadedListener());
//    }
//
//    private void initAdPlayer() {
//        mVideoAdPlayer = new VideoAdPlayer() {
//            @Override
//            public void playAd() {
//                if (mIsAdPlaying) {
//                    playerStaus.showPlayer();
//                } else {
//                    mIsAdPlaying = true;
//                    playerStaus.showPlayer();
//                }
//            }
//
//            @Override
//            public void loadAd(String s) {
//
//            }
//
//            @Override
//            public void stopAd() {
//
//            }
//
//            @Override
//            public void pauseAd() {
//
//            }
//
//            @Override
//            public void resumeAd() {
//
//            }
//
//            @Override
//            public void addCallback(VideoAdPlayerCallback videoAdPlayerCallback) {
//                mAdCallbacks.add(videoAdPlayerCallback);
//            }
//
//            @Override
//            public void removeCallback(VideoAdPlayerCallback videoAdPlayerCallback) {
//                mAdCallbacks.remove(videoAdPlayerCallback);
//            }
//
//            @Override
//            public VideoProgressUpdate getAdProgress() {
//                if (videoProgress.setVideoDuaration() <= 0 || mIsAdPlaying) {
//                    return VideoProgressUpdate.VIDEO_TIME_NOT_READY;
//                }
//                return new VideoProgressUpdate(videoProgress.setVideoCurrentPosition(), videoProgress.setVideoDuaration());
//            }
//
//            @Override
//            public int getVolume() {
//                AudioManager audioManager =
//                        (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//                if (audioManager != null) {
//                    double volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//                    double max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//                    if (max <= 0) {
//                        return 0;
//                    }
//                    return (int) ((volume / max) * 100.0f);
//                }
//                return 0;
//            }
//        };
//    }
//
//    private void requestAndPlayAds(double playAdsAfterTime, String vastResponse, ViewGroup mAdUiContainer) {
//        this.adsView = mAdUiContainer;
//        initAdPlayer();
//        if (vastResponse == null || vastResponse == "") {
////            log("No VAST ad tag URL specified");
//            resumeContent();
//            return;
//        }
//        if (mAdsManager != null) {
//            mAdsManager.destroy();
//        }
//        mAdsLoader.contentComplete();
//        mAdDisplayContainer = mSdkFactory.createAdDisplayContainer();
//        mAdDisplayContainer.setPlayer(mVideoAdPlayer);
//        mAdDisplayContainer.setAdContainer(adsView);
//
////        CompanionAdSlot companionAdSlot = mSdkFactory.createCompanionAdSlot();
////        companionAdSlot.setContainer(mCompanionViewGroup);
////        companionAdSlot.setSize(728, 90);
////        ArrayList<CompanionAdSlot> companionAdSlots = new ArrayList<CompanionAdSlot>();
////        companionAdSlots.add(companionAdSlot);
////        mAdDisplayContainer.setCompanionSlots(companionAdSlots);
//
//        AdsRequest request = mSdkFactory.createAdsRequest();
//        request.setAdsResponse(vastResponse);
//        request.setAdDisplayContainer(mAdDisplayContainer);
//        request.setContentProgressProvider(new ContentProgressProvider() {
//            @Override
//            public VideoProgressUpdate getContentProgress() {
//                if (videoProgress.setVideoDuaration() <= 0 || mIsAdPlaying) {
//                    return VideoProgressUpdate.VIDEO_TIME_NOT_READY;
//                }
//                return new VideoProgressUpdate(videoProgress.setVideoCurrentPosition(), videoProgress.setVideoDuaration());
//            }
//        });
//
//        mPlayAdsAfterTime = playAdsAfterTime;
//        mAdsLoader.requestAds(request);
//    }
//
//    private void setPlayPauseOnAdTouch() {
//        adsView.setOnTouchListener(
//                new View.OnTouchListener() {
//                    public boolean onTouch(View view, MotionEvent event) {
//                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                            if (mAdsManager != null) {
//                                if (mIsAdPlaying) {
//                                    mAdsManager.pause();
//                                } else {
//                                    mAdsManager.resume();
//                                }
//                            }
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    }
//                }
//        );
//    }
//
//    private void removePlayPauseOnAdTouch() {
//        adsView.setOnTouchListener(null);
//    }
//
//    @Override
//    public void onPlay() {
//        if (mIsAdPlaying) {
//            for (VideoAdPlayer.VideoAdPlayerCallback callback : mAdCallbacks) {
//                callback.onPlay();
//            }
//        }
//    }
//
//    @Override
//    public void onPause() {
//        if (mIsAdPlaying) {
//            for (VideoAdPlayer.VideoAdPlayerCallback callback : mAdCallbacks) {
//                callback.onPause();
//            }
//        }
//    }
//
//    @Override
//    public void onResume() {
//        if (mIsAdPlaying) {
//            for (VideoAdPlayer.VideoAdPlayerCallback callback : mAdCallbacks) {
//                callback.onResume();
//            }
//        }
//    }
//
//    @Override
//    public void onCompleted() {
//        if (mIsAdPlaying) {
//            for (VideoAdPlayer.VideoAdPlayerCallback callback : mAdCallbacks) {
//                callback.onEnded();
//            }
//        }
//        mAdsLoader.contentComplete();
//    }
//
//    @Override
//    public void onError() {
//        if (mIsAdPlaying) {
//            for (VideoAdPlayer.VideoAdPlayerCallback callback : mAdCallbacks) {
//                callback.onError();
//            }
//        }
//    }
//
//    private void pauseContent() {
//        mIsAdPlaying = true;
//        setPlayPauseOnAdTouch();
//    }
//
//    private void resumeContent() {
//        mIsAdPlaying = false;
//        removePlayPauseOnAdTouch();
//    }
//
//    @Override
//    public void startAdsVod(String placement, String contentId, String uuid, final ViewGroup mAdUiContainer, VpaidView vpaidView) {
//        String adTag = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/ad_rule_samples&ciu_szs=300x250&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=vmap&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ar%3Dpremidpost&cmsid=496&vid=short_onecue&correlator=";
//        Request requestAds = new Request.Builder()
//                .url(adTag)
//                .build();
//        client.newCall(requestAds).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                requestAndPlayAds(-1, vastResponse, mAdUiContainer);
//                Log.e("getAdsLoader " + this.getClass().getSimpleName(), "getAds: ", e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.body() != null) {
//                    vastResponse = response.body().string();
//                    requestAndPlayAds(-1, vastResponse, mAdUiContainer);
//                }
//            }
//        });
//    }
//
//    @Override
//    public void stopAds() {
//
//    }
//}
