package com.drowsyatmidnight.haint.android_firestore_sdk.utils;

public class Utils {
    public static String nullToEmpty(Object object) {
        return object == null ? "" : object.toString().trim();
    }

    public static boolean isEmpty(Object object) {
        return object == null || (object instanceof String && ((String) object).trim().isEmpty());
    }
}
