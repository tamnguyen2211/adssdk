package com.drowsyatmidnight.haint.android_fplay_ads_sdk;

import android.content.Context;
import android.webkit.WebView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

public class Utils {
    public static final String TAG = "FADS_SDK ";

    public static String nullToEmpty(Object object) {
        return object == null ? "" : object.toString().trim();
    }

    public static boolean isEmpty(Object object) {
        return object == null ? true : (object instanceof String && ((String) object).trim().isEmpty());
    }

    public static String getUserAgent(Context context) {
        return new WebView(context).getSettings().getUserAgentString();
    }

    public static String buildVodAdsUrl(String uuid, int placement, String url, Context context) {
        String ua;
        String urlEncode;
        try {
            ua = nullToEmpty(URLEncoder.encode(getUserAgent(context), "UTF-8"));
            urlEncode = nullToEmpty(URLEncoder.encode(url, "UTF-8"));
        } catch (Exception e) {
            ua = "";
            urlEncode = "";
        }
        return "https://d7.adsplay.xyz/delivery?uid=" + nullToEmpty(uuid) +
                "&pid=" + nullToEmpty(placement) +
                "&ip=" + nullToEmpty(getIPAddress(true)) +
                "&ua=" + ua +
                "&purl=" + urlEncode +
                "&cb=" + System.currentTimeMillis() +
                "&ver=1.0.0";
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
    }

    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) buf.append(String.format("%02X:", aMac));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
    }
}
