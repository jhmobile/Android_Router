package com.jinhui365.router.utils;

import android.util.Log;

/**
 * Name:RLog
 * Author:jmtian
 * Commemt:router log  class
 * Date: 2017/10/17 17:35
 */

public class RLog {
    private static final String TAG = "Router";
    private static boolean sLoggable = false;

    public static void showLog(boolean loggable) {
        sLoggable = loggable;
    }

    public static void i(String msg) {
        if (sLoggable) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (sLoggable) {
            Log.i(tag, msg);
        }
    }

    public static void w(String msg) {
        if (sLoggable) {
            Log.w(TAG, msg);
        }
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void e(String msg, Throwable tr) {
        Log.e(TAG, msg, tr);
    }
}
