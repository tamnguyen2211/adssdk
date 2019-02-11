package com.drowsyatmidnight.haint.android_banner_sdk;

import android.os.Handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BannerManager {

    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT;
    private static final int CORE_POOL_SIZE = 8;
    private static final int MAXIMUM_POOL_SIZE = 8;
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private final BlockingQueue<Runnable> getDeliveryWorkQueue;
    private final Queue<BannerTask> bannerTaskWorkQueue;
    private final ThreadPoolExecutor getDeliveryThreadPool;
    private Handler handler;
    private static BannerManager instance = null;

    static {
        KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
        instance = new BannerManager();
    }

    private BannerManager() {
        getDeliveryWorkQueue = new LinkedBlockingDeque<>();
        bannerTaskWorkQueue = new LinkedBlockingDeque<>();
        getDeliveryThreadPool = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, getDeliveryWorkQueue);

//        handler = new Handler(Looper.getMainLooper()) {
//            @Override
//            public void handleMessage(Message msg) {
//
//            }
//        };
    }

    private List<BannerInfo> bannerInfoList;

    public static BannerManager getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        List<String> urlList = new ArrayList<>();
        urlList.add("https://d.adsplay.net/delivery?uuid=897fcb2a27d84ca0abd0571889e8e39b&placement=302&referrer=https%3A%2F%2Ffptplay.vn%2F&url=https%3A%2F%2Ffptplay.vn%2Fxem-video%2Fbau-troi-tinh-yeu-sky-of-love-5acd6f4f5583200884530a55.html%23tap-1%2Fs%2F0%2Fk%2Fb%25E1%25BA%25A7u%2520tr%25E1%25BB%259Di%2520t%25C3%25ACnh%2Fsug&cxt=B%E1%BA%A7u%20Tr%E1%BB%9Di%20T%C3%ACnh%20Y%C3%AAu%20-%20Sky%20Of%20Love&cxkw=N%E1%BB%95i%20b%E1%BA%ADt%2CH%C3%A0i%20h%C6%B0%E1%BB%9Bc%2CT%C3%A2m%20l%C3%BD&ut=0&cid=5acd6f4f5583200884530a55&loc=vn-south&t=1548141873540&at=tvc");
        urlList.add("https://jsonplaceholder.typicode.com/posts/42");
        OkHttpClient client = new OkHttpClient();
        for (int i = 0; i < 2; i++) {
            Request request = new Request.Builder()
                    .url(urlList.get(i))
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    System.out.println(response.body().string().replaceAll("\n", ""));
                }
            });
        }
    }
}