package com.drowsyatmidnight.haint.android_fplay_ads_sdk;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.drowsyatmidnight.haint.android_firestore_sdk.AdsLive;
import com.drowsyatmidnight.haint.android_firestore_sdk.LiveAdsListener;
import com.drowsyatmidnight.haint.android_vpaid_sdk.VpaidView;
import com.drowsyatmidnight.haint.android_vpaid_sdk.VpaidViewListener;
import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.ads.interactivemedia.v3.api.AdsLoader;
import com.google.ads.interactivemedia.v3.api.AdsManager;
import com.google.ads.interactivemedia.v3.api.AdsManagerLoadedEvent;
import com.google.ads.interactivemedia.v3.api.AdsRequest;
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.ads.interactivemedia.v3.api.player.ContentProgressProvider;
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdsController implements AdEvent.AdEventListener, AdErrorEvent.AdErrorListener, AdsListener.AdsStatus, AdsListener.VideoStatus, VpaidViewListener, VpaidViewListener.StatusListener {
    private String vastResponse;
    private static AdsController adsController;
    private ImaSdkFactory mSdkFactory;
    private AdsLoader mAdsLoader;
    private AdsManager mAdsManager;
    private ViewGroup mAdUiContainer;
    private boolean mIsAdDisplayed;
    private AdsListener.VideoProgress videoProgress;
    private AdsListener.PlayerStaus playerStaus;
    private Context context;
    private OkHttpClient client;
    private VpaidView vpaidView;
    private VpaidViewListener vpaidViewListener;
    private AdsLive adsLive;


    public static AdsController getInstance() {
        return adsController != null ? adsController : new AdsController();
    }

    public AdsController init(Context context, AdsListener.VideoProgress videoProgress, AdsListener.PlayerStaus playerStaus) {
        return new AdsController(context, videoProgress, playerStaus);
    }

    public AdsController() {
    }

    private AdsController(Context context, AdsListener.VideoProgress videoProgress, AdsListener.PlayerStaus playerStaus) {
        this.context = context;
        this.videoProgress = videoProgress;
        this.playerStaus = playerStaus;
        client = new OkHttpClient();
        mSdkFactory = ImaSdkFactory.getInstance();
        mAdsLoader = mSdkFactory.createAdsLoader(context);
        mAdsLoader.addAdErrorListener(this);
        mAdsLoader.addAdsLoadedListener(new AdsLoader.AdsLoadedListener() {
            @Override
            public void onAdsManagerLoaded(AdsManagerLoadedEvent adsManagerLoadedEvent) {
                mAdsManager = adsManagerLoadedEvent.getAdsManager();
                mAdsManager.addAdEventListener(AdsController.this);
                mAdsManager.addAdErrorListener(AdsController.this);
                mAdsManager.init();
            }
        });
        this.vpaidViewListener = this;
    }

    private void requestAds(String adsUrlOrResponse, boolean isLiveAds) {
        AdDisplayContainer adDisplayContainer = mSdkFactory.createAdDisplayContainer();
        adDisplayContainer.setAdContainer(mAdUiContainer);

//        CompanionAdSlot companionAdSlot = mSdkFactory.createCompanionAdSlot();
//        companionAdSlot.setContainer(mCompanionViewGroup);
//        companionAdSlot.setSize(728, 90);
//        ArrayList<CompanionAdSlot> companionAdSlots = new ArrayList<>();
//        companionAdSlots.add(companionAdSlot);
//        adDisplayContainer.setCompanionSlots(companionAdSlots);

        AdsRequest request = mSdkFactory.createAdsRequest();
        if (isLiveAds) {
            request.setAdTagUrl(adsUrlOrResponse);
        } else {
            request.setAdsResponse(adsUrlOrResponse);
        }
        request.setAdDisplayContainer(adDisplayContainer);
        request.setContentProgressProvider(new ContentProgressProvider() {
            @Override
            public VideoProgressUpdate getContentProgress() {
                if (videoProgress.setVideoDuaration() <= 0 || mIsAdDisplayed) {
                    return VideoProgressUpdate.VIDEO_TIME_NOT_READY;
                }
                return new VideoProgressUpdate(videoProgress.setVideoCurrentPosition(), videoProgress.setVideoDuaration());
            }
        });
        mAdsLoader.requestAds(request);
    }

    @Override
    public void onAdError(AdErrorEvent adErrorEvent) {
        Log.e(Utils.TAG + this.getClass().getSimpleName(), "Ad Error: " + adErrorEvent.getError().getMessage());
        removePlayPauseOnAdTouch();
        if (adErrorEvent.getError().getMessage().equals("Linear assets were found in the VAST ad response, but none of them matched the video player's capabilities.")) {
            vpaidViewListener.init();
        }
    }

    @Override
    public void onAdEvent(AdEvent adEvent) {
        Log.i(Utils.TAG + this.getClass().getSimpleName(), "Event: " + adEvent.getType());
        switch (adEvent.getType()) {
            case LOADED:
                mAdsManager.start();
                break;
            case LOG:
                Map<String, String> adData = adEvent.getAdData();
                String message = "AdEvent: " + adData;
//                Log.i(TAG, message);
                if (adData.containsKey("errorMessage")) {
                    if (adData.get("errorMessage").equals("Linear assets were found in the VAST ad response, but none of them matched the video player's capabilities.")) {
                        vpaidViewListener.init();
                    }
                }
                break;
            case CONTENT_PAUSE_REQUESTED:
                mIsAdDisplayed = true;
                setPlayPauseOnAdTouch();
                playerStaus.hiddenPlayer();
                break;
            case CONTENT_RESUME_REQUESTED:
                mIsAdDisplayed = false;
                removePlayPauseOnAdTouch();
                playerStaus.showPlayer();
                break;
            case ALL_ADS_COMPLETED:
                if (mAdsManager != null) {
                    mAdsManager.destroy();
                    mAdsManager = null;
                }
                break;
            case PAUSED:
                mIsAdDisplayed = false;
                break;
            case RESUMED:
                mIsAdDisplayed = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void startAdsVod(int placement, String contentId, String uuid, ViewGroup mAdUiContainer, VpaidView vpaidView) {
        this.vpaidView = vpaidView;
        this.mAdUiContainer = mAdUiContainer;
        String adTag = "https://d.adsplay.net/delivery?placement=306&ctid=5acd6f4f5583200884530a55&catid=tv-show&uuid=6f9a9581913dcc4e&ut=2&t=1537845667&cid=5acd6f4f5583200884530a55&verison_app=3.3.1";
        Request requestAds = new Request.Builder()
                .url(adTag)
                .build();
        client.newCall(requestAds).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requestAds(vastResponse, false);
                Log.e("getAdsLoader " + AdsController.class.getSimpleName(), "getAds: ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null) {
                    vastResponse = response.body().string();
                    requestAds(vastResponse, false);
                }
            }
        });
    }

    @Override
    public void startAdsLiveTV(int placement, String channelId, String deviceNameOnCloudFirestore, String uuid, ViewGroup mAdUiContainer) {
        this.mAdUiContainer = mAdUiContainer;
        adsLive = AdsLive.getInstance();
        adsLive.init(uuid, placement, deviceNameOnCloudFirestore);
        adsLive.setChannelId(channelId);
        adsLive.getAdsLiveEvent(new LiveAdsListener() {
            @Override
            public void getAdsSuccess(String s) {
                requestAds(s,true);
            }

            @Override
            public void getAdsFailure(String s) {
                requestAds("",true);
            }
        });
    }

    @Override
    public void stopAds() {
        if (mAdsManager != null) {
            mAdsManager.discardAdBreak();
            mAdsManager.destroy();
        }
        if (adsLive != null) {
            adsLive.detachAdsLive();
        }
    }

    @Override
    public void onPlay() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onCompleted() {
        if (mAdsLoader != null) {
            mAdsLoader.contentComplete();
        }
    }

    private void setPlayPauseOnAdTouch() {
        mAdUiContainer.setOnTouchListener(
                new View.OnTouchListener() {
                    public boolean onTouch(View view, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            if (mAdsManager != null) {
                                if (mIsAdDisplayed) {
                                    mAdsManager.pause();
                                } else {
                                    mAdsManager.resume();
                                }
                            }
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
        );
    }

    private void removePlayPauseOnAdTouch() {
        mAdUiContainer.setOnTouchListener(null);
    }

    @Override
    public void init() {
        vpaidView.initView(mAdUiContainer.getWidth(), mAdUiContainer.getHeight(), Integer.parseInt(String.valueOf(videoProgress.setVideoCurrentPosition())), vastResponse);
    }

    @Override
    public void adsReady() {
        playerStaus.hiddenPlayer();
    }

    @Override
    public void adsComplete() {
        playerStaus.showPlayer();
    }

    @Override
    public void adsError() {
        playerStaus.showPlayer();
    }

    @Override
    public void adsCanSkip() {

    }
}
