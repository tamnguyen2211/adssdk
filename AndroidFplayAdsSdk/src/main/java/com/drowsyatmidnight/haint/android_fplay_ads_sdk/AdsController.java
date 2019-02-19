package com.drowsyatmidnight.haint.android_fplay_ads_sdk;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.drowsyatmidnight.haint.android_firestore_sdk.AdsLive;
import com.drowsyatmidnight.haint.android_firestore_sdk.LiveAdsListener;
import com.drowsyatmidnight.haint.android_vpaid_sdk.IMAJsListener;
import com.drowsyatmidnight.haint.android_vpaid_sdk.VmapParser;
import com.drowsyatmidnight.haint.android_vpaid_sdk.VpaidView;
import com.drowsyatmidnight.haint.android_vpaid_sdk.VpaidViewListener;
import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.ads.interactivemedia.v3.api.AdsLoader;
import com.google.ads.interactivemedia.v3.api.AdsManager;
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

//firestore
//vpaid
//gg ima

public class AdsController implements AdEvent.AdEventListener, AdErrorEvent.AdErrorListener, AdsListener.AdsStatus, AdsListener.VideoStatus, VpaidViewListener, VpaidViewListener.StatusListener, AdsListener.SkipButtonStatus {
    private String vastResponse = "";
    private static AdsController adsController;
    private ImaSdkFactory mSdkFactory;
    private AdsLoader mAdsLoader;
    private AdsManager mAdsManager;
    private ViewGroup mAdUiContainer;
    private boolean mIsAdDisplayed;
    private AdsListener.VideoProgress videoProgress;
    private AdsListener.PlayerStaus playerStaus;
    private AdsListener.SkipButtonStatus skipButtonStatus;
    private Context context;
    private OkHttpClient client;
    private VpaidView vpaidView;
    private VpaidViewListener vpaidViewListener;
    private AdsLive adsLive;
    private int skipOffset;
    private Button skipButton;


    public static AdsController getInstance() {
        return adsController != null ? adsController : new AdsController();
    }

    public static AdsController init(Context context, AdsListener.VideoProgress videoProgress, AdsListener.PlayerStaus playerStaus) {
        return new AdsController(context, videoProgress, playerStaus);
    }

    private AdsController() {
    }

    private AdsController(Context context, AdsListener.VideoProgress videoProgress, AdsListener.PlayerStaus playerStaus) {
        this.context = context;
        this.videoProgress = videoProgress;
        this.playerStaus = playerStaus;
        this.skipButtonStatus = this;
        client = new OkHttpClient();
        mSdkFactory = ImaSdkFactory.getInstance();
        mAdsLoader = mSdkFactory.createAdsLoader(context);
        mAdsLoader.addAdErrorListener(this);
        mAdsLoader.addAdsLoadedListener(adsManagerLoadedEvent -> {
            mAdsManager = adsManagerLoadedEvent.getAdsManager();
            mAdsManager.addAdEventListener(AdsController.this);
            mAdsManager.addAdErrorListener(AdsController.this);
            mAdsManager.init();
        });
        this.vpaidViewListener = this;
        IMAJsListener.initAndroidJsListener(this);
    }

    private void requestAds(String adsUrlOrResponse, boolean isLiveAds) {
        if (adsUrlOrResponse == null || adsUrlOrResponse == "") {
            return;
        }
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
        Log.i(AdsController.class.getSimpleName(), "requestAds: " + adsUrlOrResponse);
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
//        Log.i(Utils.TAG + this.getClass().getSimpleName(), "Event: " + adEvent.getType());
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
            case STARTED:
                if (!Utils.isEmpty(vastResponse)) {
                    skipOffset = VmapParser.getSkipOffSet(vastResponse);
                    Log.i(AdsController.class.getSimpleName(), "skipOffset: " + skipOffset);
                    if (adEvent.getAd().isSkippable() && skipOffset == 0) {
                        mAdsManager.focusSkipButton();
                    } else {
                        if (skipOffset > 0) {
                            new CountDownTimer(skipOffset * 1000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    --skipOffset;
                                    Log.i(AdsController.class.getSimpleName(), "skipOffset: " + skipOffset);
                                    skipButtonStatus.showSkipButton(skipOffset);
                                }

                                @Override
                                public void onFinish() {
                                    skipOffset = 0;
                                    Log.i(AdsController.class.getSimpleName(), "skipOffset: onFinish()");
                                    skipButtonStatus.addActionSkipbutton();
                                    skipButton.requestFocus();
                                }
                            }.start();
                        }
                    }
                }
                break;
            case CONTENT_RESUME_REQUESTED:
                mIsAdDisplayed = false;
                removePlayPauseOnAdTouch();
                playerStaus.showPlayer();
                skipButtonStatus.hiddenSkipButton();
                break;
            case ALL_ADS_COMPLETED:
                Log.d("Debug vpaid", "onAdEvent: All ads complete");
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
    public void startAdsVod(String uuid, int placement, String url, ViewGroup mAdUiContainer, VpaidView vpaidView, View skipButton) {
        this.vpaidView = vpaidView;
        this.mAdUiContainer = mAdUiContainer;
        this.skipButton = (Button) skipButton;
        String adTag = Utils.buildVodAdsUrl(uuid, placement, url, context);
        Log.i("UrlDelivery: ", adTag);
//        String adTag = "https://d7.adsplay.xyz/delivery?uid=4909bd7e4b684636&pid=308&ip=192.168.1.163&ua=Mozilla%2F5.0+%28Linux%3B+Android+9%3B+FPT+ATV+Play+Box+Build%2FNHG47L%3B+wv%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Version%2F4.0+Chrome%2F71.0.3578.99+Mobile+Safari%2F537.36&purl=http%3A%2F%2Ffptplay.net%2Fxem-video%2Fhoa-vuong-pha-hieu-chi-chien-the-king-of-blaze-5bfaa031fa9c5e0a1e269a4b.html%231&cb=1550476969020&ver=1.0.0";
        Request requestAds = new Request.Builder()
                .url(adTag)
                .build();
        client.newCall(requestAds).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requestAds(vastResponse, false);
                Log.e("getAdsLoader " + AdsController.class.getSimpleName(), "url: " + adTag + "getAds: ", e);
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
    public void startAdsLiveTV(String uuid, int placement, String channelId, String deviceNameOnCloudFirestore, ViewGroup mAdUiContainer) {
        this.mAdUiContainer = mAdUiContainer;
        adsLive = AdsLive.getInstance();
        adsLive.init(uuid, placement, deviceNameOnCloudFirestore);
        adsLive.setChannelId(channelId);
        adsLive.getAdsLiveEvent(new LiveAdsListener() {
            @Override
            public void getAdsSuccess(String s) {
                requestAds(s + "&ver=1.0.0", true);
                Log.i(AdsController.class.getSimpleName(), "getAdsSuccess: " + s + "&ver=1.0.0");
            }

            @Override
            public void getAdsFailure(String s) {
                requestAds("", true);
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
    public void destroyAds() {
        if (mAdsManager != null) {
            mAdsManager.discardAdBreak();
            mAdsManager.destroy();
        }
        if (adsLive != null) {
            adsLive.detachAdsLive();
        }
        adsController = null;
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
                (view, event) -> {
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
        if (vpaidView != null) {
            new Handler(Looper.getMainLooper()).post(() -> {
                playerStaus.hiddenPlayer();
                vpaidView.setVisibility(View.VISIBLE);
            });
        }
    }

    @Override
    public void adsComplete() {
        if (vpaidView != null) {
            new Handler(Looper.getMainLooper()).post(() -> {
                playerStaus.showPlayer();
                vpaidView.setVisibility(View.GONE);
            });
        }
    }

    @Override
    public void adsError() {
        if (vpaidView != null) {
            new Handler(Looper.getMainLooper()).post(() -> {
                playerStaus.showPlayer();
                vpaidView.setVisibility(View.GONE);
            });
        }
    }

    @Override
    public void adsCanSkip() {

    }

    @Override
    public void hiddenSkipButton() {
        if (skipButton != null) {
            new Handler(Looper.getMainLooper()).post(() -> {
                skipButton.setVisibility(View.GONE);
                Log.i(AdsController.class.getSimpleName(), "showSkipButton(): hide skip");
                skipButton.setOnClickListener(null);
            });
        }
    }

    @Override
    public void showSkipButton(int skipOffset) {
        if (skipButton != null) {
            new Handler(Looper.getMainLooper()).post(() -> {
                skipButton.setVisibility(View.VISIBLE);
                Log.i(AdsController.class.getSimpleName(), "showSkipButton(): show skip");
                skipButton.setText("You can skip this ad in: " + skipOffset);
            });
        }
    }

    @Override
    public void addActionSkipbutton() {
        if (skipButton != null) {
            skipButton.requestFocus();
            skipButton.setText("Skip Ad");
            Log.i(AdsController.class.getSimpleName(), "showSkipButton(): request focus");
            new Handler(Looper.getMainLooper()).post(() -> {
                skipButton.setOnClickListener(v -> {
                    mAdsManager.skip();
                    mAdsManager.discardAdBreak();
                });
            });
        }
    }
}
