package com.drowsyatmidnight.haint.android_vpaid_sdk;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class VpaidView extends WebView {
    public VpaidView(Context context) {
        super(context);
        initWebView();
    }

    public VpaidView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView();
    }

    public VpaidView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        this.addJavascriptInterface(new IMAJsListener(this), "Android");
    }

//    public void initView(final int width, final int height, final String vastUrl) {
//        this.getLayoutParams().width = width;
//        this.getLayoutParams().height = height;
//        this.requestLayout();
//        this.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
//                Log.d("WebView", "Debuggggggggggggggg: " + consoleMessage.message() + " -- From line "
//                        + consoleMessage.lineNumber() + " of "
//                        + consoleMessage.sourceId());
//                return true;
//            }
//        });
//        this.loadDataWithBaseURL("http://localhost/", RenderingIMA.getHtmlWithVastUrl(width, height, vastUrl), "text/html", "utf-8", null);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == event.KEYCODE_DPAD_CENTER || keyCode == event.KEYCODE_ENTER) && IMAJsListener.canSkip) {
            this.loadUrl("javascript:skipAds()");
            Log.d("WebView", "Debuggggggggggggggg: onKeyDown");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void initView(final int width, final int height, int milis, final String vastResponse) {
        this.getLayoutParams().width = width;
        this.getLayoutParams().height = height;
        this.requestLayout();
        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("WebView", "Debuggggggggggggggg: " + consoleMessage.message() + " -- From line "
                        + consoleMessage.lineNumber() + " of "
                        + consoleMessage.sourceId());
                return true;
            }
        });
        try {
//            int offsetSecond = 0;
//            String vast = null;
//            Map<Integer, String> vastData = VmapParser.getCorrectVastXML(milis, vastResponse);
//            if (vastData.size() == 1) {
//                for (Integer second : vastData.keySet()) {
//                    offsetSecond = second;
//                    vast = vastData.get(second);
//                }
//            }
                this.loadDataWithBaseURL("http://localhost/", RenderingIMA.getHtmlWithVastContent(width, height, vastResponse, VmapParser.getSkipOffSet(vastResponse), milis / 1000), "text/html", "utf-8", null);
        } catch (Exception e) {
            Log.d("VpaidParsing", "Debuggggggggggggggg: " + e.getMessage());
            this.loadDataWithBaseURL("http://localhost/", RenderingIMA.getHtmlWithVastContent(width, height, "", 0, milis / 1000), "text/html", "utf-8", null);
        }
    }
}
