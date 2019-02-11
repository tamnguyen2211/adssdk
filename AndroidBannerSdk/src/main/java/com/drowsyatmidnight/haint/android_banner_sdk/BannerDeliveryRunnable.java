package com.drowsyatmidnight.haint.android_banner_sdk;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BannerDeliveryRunnable implements Runnable {
    private static final int READ_SIZE = 1024 * 2;
    static final int GET_DELIVERY_FAILED = -1;
    static final int GET_DELIVERY_STARTED = 0;
    static final int GET_DELIVERY_COMPLETED = 1;
    private static final long SLEEP_TIME_MILLISECONDS = 250;

    private final TaskRunnableGetDelveiryMethod taskRunnableGetDelveiryMethod;

    interface TaskRunnableGetDelveiryMethod {
        void setGetDeliveryThread(Thread currentThread);

        void handledGetDeliveryState(int state);

        void setBannerInfo(BannerInfo bannerInfo);

        BannerInfo getBannerInfo();
    }

    public BannerDeliveryRunnable(TaskRunnableGetDelveiryMethod taskRunnableGetDelveiryMethod) {
        this.taskRunnableGetDelveiryMethod = taskRunnableGetDelveiryMethod;
    }

    private BannerInfo getBannerInfoWithoutResponse(BannerInfo bannerInfo) {
        bannerInfo.setResponse("");
        return bannerInfo;
    }

    @Override
    public void run() {
        taskRunnableGetDelveiryMethod.setGetDeliveryThread(Thread.currentThread());
        BannerInfo bannerInfo = taskRunnableGetDelveiryMethod.getBannerInfo();
        try {
            taskRunnableGetDelveiryMethod.handledGetDeliveryState(GET_DELIVERY_STARTED);
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("")
                    .build();
            if (Thread.interrupted()) {
                bannerInfo = getBannerInfoWithoutResponse(bannerInfo);
                return;
            }
            Response response = okHttpClient.newCall(request).execute();
            if (Thread.interrupted()) {
                bannerInfo = getBannerInfoWithoutResponse(bannerInfo);
                return;
            }
            if (response.isSuccessful()) {
                bannerInfo.setResponse(response.body().string());
            } else {
                bannerInfo = getBannerInfoWithoutResponse(bannerInfo);
            }
            Thread.sleep(SLEEP_TIME_MILLISECONDS);
        } catch (Exception e) {
            bannerInfo = getBannerInfoWithoutResponse(bannerInfo);
        } finally {
            if (bannerInfo.getResponse() != null && bannerInfo.getResponse().equals("")) {
                taskRunnableGetDelveiryMethod.setBannerInfo(bannerInfo);
                taskRunnableGetDelveiryMethod.handledGetDeliveryState(GET_DELIVERY_COMPLETED);
            } else {
                taskRunnableGetDelveiryMethod.setBannerInfo(bannerInfo);
                taskRunnableGetDelveiryMethod.handledGetDeliveryState(GET_DELIVERY_FAILED);
            }
            taskRunnableGetDelveiryMethod.setGetDeliveryThread(null);
            Thread.interrupted();
        }
    }
}
