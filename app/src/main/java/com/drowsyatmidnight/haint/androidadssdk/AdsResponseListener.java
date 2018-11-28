package com.drowsyatmidnight.haint.androidadssdk;

import java.io.IOException;

public interface AdsResponseListener {
    void onSuccess(String response);
    void onFailure(IOException e);
}
