package com.drowsyatmidnight.haint.android_firestore_sdk;

import android.support.annotation.Nullable;
import android.util.Log;

import com.drowsyatmidnight.haint.android_firestore_sdk.model.CloudFireStoreModel;
import com.drowsyatmidnight.haint.android_firestore_sdk.utils.Utils;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class CloudFireStoreManager {
    private FirebaseFirestore db;
    private CloudFireStoreModel cloudFireStoreModel;
    private boolean isWatching;
    private String uuid;
    private int placement;
    private String device;
    private LiveAdsListener liveAdsListener;
    private ListenerRegistration listenerRegistration;
    private String channelId = "";
    private static final String TAG = "LinhTinh";

    public CloudFireStoreManager(String uuid, int placement, String device) {
        this.db = FirebaseFirestore.getInstance();
        this.uuid = uuid;
        this.placement = placement;
        this.device = device;
    }

    public CloudFireStoreManager(String uuid, int placement, String device, FirebaseFirestore db) {
        this.db = db;
        this.uuid = uuid;
        this.placement = placement;
        this.device = device;
    }

    public void setLiveAdsListener(LiveAdsListener liveAdsListener) {
        this.liveAdsListener = liveAdsListener;
    }

    public void getAdsLive(String channelId) {
        if (!this.channelId.equals(channelId)) {
            this.channelId = channelId;
            isWatching = false;
            if (listenerRegistration != null) {
                listenerRegistration.remove();
            }
        }
        listenerRegistration = db.collection(AdsConst.FB_COLLECTION_NAME)
                .document(channelId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            liveAdsListener.getAdsFailure(e.getMessage());
                            Log.e(TAG, "onEvent: ", e);
                        } else {
                            if (documentSnapshot.exists()) {
                                cloudFireStoreModel = documentSnapshot.toObject(CloudFireStoreModel.class);
                                if (!Utils.isEmpty(cloudFireStoreModel)) {
                                    Log.d(TAG, "\nactive: " + cloudFireStoreModel.is_active + "\npush_watching: " + cloudFireStoreModel.push_watching + "\npush_new: " + cloudFireStoreModel.push_new + "\nisWatching: " + isWatching);
                                    if (cloudFireStoreModel.devices.contains(device)) {
                                        String url = Utils.nullToEmpty(makeUrl(cloudFireStoreModel));
                                        if (!Utils.isEmpty(url)) {
                                            liveAdsListener.getAdsSuccess(url);
                                        } else {
                                            liveAdsListener.getAdsFailure("Empty url");
                                        }
                                    }
                                } else {
                                    liveAdsListener.getAdsFailure("Invalid data format from cloud fire store");
                                }
                            } else {
                                liveAdsListener.getAdsFailure("Invalid channel id from cloud fire store");
                            }
                        }
                    }
                });
    }

    public void detachAdsLive() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

    private String makeUrl(CloudFireStoreModel cloudFireStoreModel) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        try {
            long fromDate = dateFormat.parse(cloudFireStoreModel.from_date).getTime() / 1000;
            long toDate = dateFormat.parse(cloudFireStoreModel.to_date).getTime() / 1000;
            long curDate = Calendar.getInstance().getTimeInMillis() / 1000;
            if (cloudFireStoreModel.is_active == 1) {
                if (fromDate <= curDate && curDate <= toDate) {
                    if ((cloudFireStoreModel.push_watching == 1 && isWatching) || (cloudFireStoreModel.push_new == 1 && !isWatching)) {
                        isWatching = true;
                        return cloudFireStoreModel.url + "&uuid=" + uuid + "&placementId=" + placement;
                    } else {
                        isWatching = true;
                        return null;
                    }
                } else {
                    isWatching = true;
                    return null;
                }
            } else {
                isWatching = true;
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
