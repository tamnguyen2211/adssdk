package com.drowsyatmidnight.haint.android_fplay_ads_sdk;

import android.view.View;
import android.view.ViewGroup;

import com.drowsyatmidnight.haint.android_vpaid_sdk.VpaidView;

public interface AdsListener {

    interface VideoProgress {
        long setVideoDuaration();
        long setVideoCurrentPosition();
    }

    interface PlayerStaus {
        void hiddenPlayer();
        void showPlayer();
    }

    interface SkipButtonStatus{
        void hiddenSkipButton();
        void showSkipButton(int skipOffset);
        void addActionSkipbutton();
    }

    interface VideoStatus{
        void onPlay();
        void onPause();
        void onResume();
        void onError();
        void onCompleted();
    }

    interface AdsStatus {
        void startAdsVod(String uuid, int placement, String url, ViewGroup mAdUiContainer, VpaidView vpaidView, View skipButton);
        void startAdsLiveTV(String uuid, int placement, String channelId, String deviceNameOnCloudFirestore, ViewGroup mAdUiContainer);
        void stopAds();
        void destroyAds();
    }
}
