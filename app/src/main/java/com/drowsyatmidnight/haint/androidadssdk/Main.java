package com.drowsyatmidnight.haint.androidadssdk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.drowsyatmidnight.haint.android_vpaid_sdk.IMAJsListener;
import com.drowsyatmidnight.haint.android_vpaid_sdk.VpaidView;
import com.drowsyatmidnight.haint.android_vpaid_sdk.VpaidViewListener;
import com.google.android.exoplayer2.ui.PlayerView;

import java.io.IOException;

public class Main extends Activity implements VpaidViewListener, VpaidViewListener.StatusListener, AdsResponseListener {

    private PlayerView playerView;
    private PlayerManager playerManager;
    private VpaidView vpaidView;
    private FrameLayout frameLayout;
    private View playerViewOnframe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerView = findViewById(R.id.video_view);
        playerManager = new PlayerManager(this, this);
        vpaidView = findViewById(R.id.vpaidView);
        vpaidView.setVisibility(View.GONE);
        ImaAdsLoader.addImaAdsLoaderListener(this);
        IMAJsListener.initAndroidJsListener(this);
        frameLayout = findViewById(R.id.framelayout);
        playerViewOnframe = playerView.getRootView();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        playerManager.reset();
    }

    @Override
    public void onDestroy() {
        playerManager.release();
        super.onDestroy();
    }

    @Override
    public void init() {
        vpaidView.initView(playerView.getWidth(), playerView.getHeight(), Integer.parseInt(String.valueOf(playerManager.player.getContentPosition())), playerManager.adsResponse);
    }

    @Override
    public void adsReady() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                vpaidView.setVisibility(View.VISIBLE);
                playerManager.player.setPlayWhenReady(false);
                frameLayout.removeView(playerViewOnframe);
            }
        });
    }

    @Override
    public void adsComplete() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                vpaidView.setVisibility(View.GONE);
                playerManager.player.setPlayWhenReady(true);
                frameLayout.addView(playerView, playerView.getLayoutParams());
            }
        });
    }

    @Override
    public void adsError() {
        playerManager.player.setPlayWhenReady(true);
    }

    @Override
    public void adsCanSkip() {

    }

    @Override
    public void onSuccess(final String response) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playerManager.setAdsResponse(response);
                playerManager.init(playerView);
            }
        });
    }

    @Override
    public void onFailure(IOException e) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playerManager.setAdsResponse("");
                playerManager.init(playerView);
            }
        });
    }
}
