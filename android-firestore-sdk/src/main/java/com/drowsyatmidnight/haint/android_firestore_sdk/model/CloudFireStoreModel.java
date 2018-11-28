package com.drowsyatmidnight.haint.android_firestore_sdk.model;

import java.util.List;

public class CloudFireStoreModel {
    public List<String> devices;
    public String from_date;
    public int is_active;
    public int push_watching;
    public int push_new;
    public String pushed_at;
    public String to_date;
    public String url;

    @Override
    public String toString() {
        return "CloudFireStoreModel{" +
                "devices=" + devices +
                ", from_date='" + from_date + '\'' +
                ", is_active=" + is_active +
                ", push_watching=" + push_watching +
                ", push_new=" + push_new +
                ", pushed_at='" + pushed_at + '\'' +
                ", to_date='" + to_date + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
