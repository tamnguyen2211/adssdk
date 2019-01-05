package com.drowsyatmidnight.haint.android_fplay_ads_sdk;

public interface AdPlayerListener {
    interface PlayerCallback {

        void onPlay();

        void onPause();

        void onResume();

        void onCompleted();

        void onError();
    }
    void onPlay();

    void onPause();

    void onResume();

    void onCompleted();

    void onError();
}
