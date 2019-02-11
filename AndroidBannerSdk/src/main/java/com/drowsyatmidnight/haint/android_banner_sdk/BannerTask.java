package com.drowsyatmidnight.haint.android_banner_sdk;

import java.lang.ref.WeakReference;

public class BannerTask implements BannerDeliveryRunnable.TaskRunnableGetDelveiryMethod {
    private WeakReference<BannerView> bannerWeakRef;
    private Thread threadThis;
    private Runnable bannerDeliveryRunnable;
    private Thread currentThread;
    private static BannerManager bannerManager;

    private BannerInfo mbannerInfo;

    public BannerTask() {
        bannerDeliveryRunnable = new BannerDeliveryRunnable(this);
        bannerManager = BannerManager.getInstance();
    }

    void initializeGetDeliveryWorkerTask(BannerManager mBannerManager, BannerView bannerView){
        bannerManager = mBannerManager;
        bannerWeakRef = new WeakReference<>(bannerView);

    }

    public Thread getCurrentThread() {
        synchronized(bannerManager) {
            return currentThread;
        }
    }

    public void setCurrentThread(Thread thread) {
        synchronized(bannerManager) {
            currentThread = thread;
        }
    }

    @Override
    public void setGetDeliveryThread(Thread currentThread) {

    }

    @Override
    public void handledGetDeliveryState(int state) {

    }

    @Override
    public void setBannerInfo(BannerInfo bannerInfo) {
        mbannerInfo = bannerInfo;
    }

    void recycle() {

        // Deletes the weak reference to the imageView
        if ( null != bannerWeakRef ) {
            bannerWeakRef.clear();
            bannerWeakRef = null;
        }

        // Releases references to the byte buffer and the BitMap
        mbannerInfo = null;
//        mDecodedImage = null;
    }

    void handleState(int state) {
//        bannerManager.handleState(this, state);
    }

    public BannerView getBannerView() {
        if ( null != bannerWeakRef ) {
            return bannerWeakRef.get();
        }
        return null;
    }

    @Override
    public BannerInfo getBannerInfo() {
        return null;
    }
}
