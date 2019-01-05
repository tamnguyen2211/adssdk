package com.drowsyatmidnight.haint.android_fplay_ads_sdk;

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

    interface VideoStatus{
        void onPlay();
        void onPause();
        void onResume();
        void onError();
        void onCompleted();
    }

    interface AdsStatus {
        void startAdsVod(int placement, String contentId, String uuid, ViewGroup mAdUiContainer, VpaidView vpaidView);
        void startAdsLiveTV(int placement, String channelId, String deviceNameOnCloudFirestore, String uuid, ViewGroup mAdUiContainer);
        void stopAds();
    }
}
