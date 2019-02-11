package com.drowsyatmidnight.haint.android_banner_sdk;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.WebView;

public class BannerView extends WebView {

    public BannerView(Context context) {
        super(context);
        initWebView();
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView();
        initWebView();
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initWebView() {
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setUseWideViewPort(true);
        this.getSettings().setLoadWithOverviewMode(true);
        this.setBackgroundColor(Color.BLACK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            this.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        this.getSettings().setDomStorageEnabled(true);
        CookieManager.getInstance().setAcceptCookie(true);
    }


}
