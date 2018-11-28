package com.drowsyatmidnight.haint.android_vpaid_sdk;

import android.webkit.JavascriptInterface;

public class IMAJsListener {
    private VpaidView vpaidView;
    private static VpaidViewListener.StatusListener vpaidViewListener;
    public static boolean canSkip = false;

    public IMAJsListener(VpaidView vpaidView) {
        this.vpaidView = vpaidView;
    }

    public static void initAndroidJsListener(VpaidViewListener.StatusListener mVpaidViewListener){
        vpaidViewListener = mVpaidViewListener;
    }

    @JavascriptInterface
    public void showToast(String toast) {

    }

    @JavascriptInterface
    public void adsCanSkip() {
        vpaidViewListener.adsCanSkip();
        canSkip = true;
    }

    @JavascriptInterface
    public void adsIsReady() {
        vpaidViewListener.adsReady();
    }

    @JavascriptInterface
    public void adsIsComplete() {
        vpaidViewListener.adsComplete();
    }

    @JavascriptInterface
    public void adsIsFailure() {
        vpaidViewListener.adsError();
    }
}
