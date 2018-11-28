package com.drowsyatmidnight.haint.android_firestore_sdk;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by haint on 10/01/2018.
 */

public class AdsLive {
    private static AdsLive adsLive;
    private CloudFireStoreManager cloudFireStoreManager;

    public static AdsLive getInstance() {
        return adsLive = (adsLive != null ? adsLive : new AdsLive());
    }

    public void init(String uuid, int placement, String device) {
        cloudFireStoreManager = new CloudFireStoreManager(uuid, placement, device);
    }

    public void init(String uuid, int placement, String device, FirebaseFirestore db) {
        cloudFireStoreManager = new CloudFireStoreManager(uuid, placement, device, db);
    }

    public void setChannelId(String channelId) {
        cloudFireStoreManager.getAdsLive(channelId);
    }

    public void getAdsLiveEvent(LiveAdsListener liveAdsListener) {
        cloudFireStoreManager.setLiveAdsListener(liveAdsListener);
    }

    public void detachAdsLive() {
        if (cloudFireStoreManager != null) {
            cloudFireStoreManager.detachAdsLive();
            adsLive = null;
        }
    }
}
