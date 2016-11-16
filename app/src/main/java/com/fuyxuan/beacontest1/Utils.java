package com.fuyxuan.beacontest1;

import android.net.ConnectivityManager;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Allison on 2016/11/16.
 */

public class Utils {
    private static boolean ENABLE_LOG = true;
    private static ConnectivityManager mConnectivityManager;

    public static void logd(String tag, String msg) {
        if (ENABLE_LOG) {
            Log.d(tag, msg);
        }
    }

    public static void logi(String tag, String msg) {
        if (ENABLE_LOG) {
            Log.i(tag, msg);
        }
    }

    public static void loge(String tag, String msg) {
        if (ENABLE_LOG) {
            Log.e(tag, msg);
        }
    }


    public static long getSecondsSinceEpochDay(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC+8"));

        return  (long)calendar.getTimeInMillis() / 1000L;
    }


}