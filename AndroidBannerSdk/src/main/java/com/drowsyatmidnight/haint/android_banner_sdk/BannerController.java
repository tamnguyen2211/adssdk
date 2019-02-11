package com.drowsyatmidnight.haint.android_banner_sdk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BannerController implements HttpListener {
    private static BannerController instance;
    private List<BannerInfo> bannerInfoList;
    private OkHttpClient okHttpClient;
    private HttpListener httpListener;
    private Context context;

    public static BannerController getInstance() {
        return instance == null ? new BannerController() : instance;
    }

    public void init(List<BannerInfo> bannerInfoList, Context context) {
        this.bannerInfoList = bannerInfoList;
        this.context = context;
        okHttpClient = new OkHttpClient();
        httpListener = this;
        showBanner();
    }

    private String readBannerTamplate() {
        if (context != null) {
            StringBuilder htmlCode = new StringBuilder();
            InputStream fIn = null;
            InputStreamReader isr = null;
            BufferedReader input = null;
            try {
                fIn = context.getResources().getAssets()
                        .open("banner.html");
                isr = new InputStreamReader(fIn);
                input = new BufferedReader(isr);
                String line = "";
                while ((line = input.readLine()) != null) {
                    htmlCode.append(line);
                }
            } catch (Exception e) {
                e.getMessage();
            } finally {
                try {
                    if (isr != null)
                        isr.close();
                    if (fIn != null)
                        fIn.close();
                    if (input != null)
                        input.close();
                } catch (Exception e2) {
                    e2.getMessage();
                }
            }
            return htmlCode.toString();
        } else {
            return null;
        }
    }

    private void showBanner() {
        if (bannerInfoList != null) {
            for (final BannerInfo bannerInfo : bannerInfoList) {
                if (okHttpClient != null) {
                    Request request = new Request.Builder()
                            .url("https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/single_ad_samples&ciu_szs=300x250&impl=s&gdfp_req=1&env=vp&output=vast&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ct%3Dlinear&correlator=")
                            .build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            httpListener.onFailure(bannerInfo);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            httpListener.onSuccess(bannerInfo);
                            bannerInfo.setResponse(readBannerTamplate());
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onSuccess(final BannerInfo bannerInfo) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                bannerInfo.getBannerView().setVisibility(View.VISIBLE);
                bannerInfo.getBannerView().loadDataWithBaseURL("http://localhost/", bannerInfo.getResponse(), "text/html", "utf-8", null);
            }
        });
    }

    @Override
    public void onFailure(final BannerInfo bannerInfo) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                bannerInfo.getBannerView().setVisibility(View.GONE);
            }
        });
    }
}
