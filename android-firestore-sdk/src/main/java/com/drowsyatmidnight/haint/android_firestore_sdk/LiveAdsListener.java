package com.drowsyatmidnight.haint.android_firestore_sdk;

public interface LiveAdsListener {
    void getAdsSuccess(String url);
    void getAdsFailure(String exception);
}
