package com.drowsyatmidnight.haint.android_banner_sdk;

public class BannerInfo {
    private BannerView bannerView;
    private int placementId;
    private String response = "";

    public BannerInfo(BannerView bannerView, int placementId) {
        this.bannerView = bannerView;
        this.placementId = placementId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public BannerView getBannerView() {
        return bannerView;
    }

    public void setBannerView(BannerView bannerView) {
        this.bannerView = bannerView;
    }

    public int getPlacementId() {
        return placementId;
    }

    public void setPlacementId(int placementId) {
        this.placementId = placementId;
    }
}
