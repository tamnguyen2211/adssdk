package com.drowsyatmidnight.haint.android_banner_sdk;

public interface HttpListener {
    void onSuccess(BannerInfo bannerInfo);
    void onFailure(BannerInfo bannerInfo);
}
