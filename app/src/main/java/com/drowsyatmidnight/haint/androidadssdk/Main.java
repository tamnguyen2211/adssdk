package com.drowsyatmidnight.haint.androidadssdk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.drowsyatmidnight.haint.android_banner_sdk.BannerController;
import com.drowsyatmidnight.haint.android_banner_sdk.BannerInfo;
import com.drowsyatmidnight.haint.android_banner_sdk.BannerView;
import com.drowsyatmidnight.haint.android_vpaid_sdk.VpaidView;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;
import java.util.List;

public class Main extends Activity {

    private PlayerView playerView;
    private PlayerManager playerManager;
    private VpaidView vpaidView;
    private Button btnVod;
    private Button btnLive1;
    private Button skipButton;
    private BannerView bannerView1;
    private BannerView bannerView2;
    private BannerController bannerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerView = findViewById(R.id.video_view);
        btnVod = findViewById(R.id.btnVod);
        btnLive1 = findViewById(R.id.btnLiveTv1);
        skipButton = findViewById(R.id.btnSkip);
        skipButton.setVisibility(View.GONE);
        playerManager = new PlayerManager(this);
        vpaidView = findViewById(R.id.vpaidView);
        vpaidView.setVisibility(View.GONE);
        bannerView1 = findViewById(R.id.banner1);
        bannerView2 = findViewById(R.id.banner2);
        bannerController = BannerController.getInstance();
        List<BannerInfo> bannerInfos = new ArrayList<>();
        bannerInfos.add(new BannerInfo(bannerView1, 102));
        bannerInfos.add(new BannerInfo(bannerView2, 202));
        bannerController.init(bannerInfos, this);
        btnVod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerManager != null) {
                    playerManager.release();
                }
                vpaidView.setVisibility(View.GONE);
                playerManager.init(playerView, vpaidView, getString(R.string.content_vod), skipButton);
            }
        });
        btnLive1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerManager != null) {
                    playerManager.release();
                }
                vpaidView.setVisibility(View.GONE);
                playerManager.init(playerView, getString(R.string.content_live_yt), "quang-tri");
            }
        });
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
