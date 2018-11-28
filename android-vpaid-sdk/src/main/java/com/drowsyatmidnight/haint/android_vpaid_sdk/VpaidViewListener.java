package com.drowsyatmidnight.haint.android_vpaid_sdk;

public interface VpaidViewListener {
    void init();
    interface StatusListener {
        void adsReady();
        void adsComplete();
        void adsError();
        void adsCanSkip();
    }
}
