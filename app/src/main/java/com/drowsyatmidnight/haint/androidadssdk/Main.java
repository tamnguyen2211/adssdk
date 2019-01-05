package com.drowsyatmidnight.haint.androidadssdk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.drowsyatmidnight.haint.android_vpaid_sdk.VpaidView;
import com.google.android.exoplayer2.ui.PlayerView;

public class Main extends Activity {

    private PlayerView playerView;
    private PlayerManager playerManager;
    private VpaidView vpaidView;
    private Button btnVod;
    private Button btnLive1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerView = findViewById(R.id.video_view);
        btnVod = findViewById(R.id.btnVod);
        btnLive1 = findViewById(R.id.btnLiveTv1);
        playerManager = new PlayerManager(this);
        vpaidView = findViewById(R.id.vpaidView);
        vpaidView.setVisibility(View.GONE);
        btnVod.setOnClickListener(v -> playerManager.init(playerView, vpaidView, getString(R.string.content_vod)));
        btnLive1.setOnClickListener(v -> playerManager.init(playerView, getString(R.string.content_live_yt), "quang-tri"));
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
}
