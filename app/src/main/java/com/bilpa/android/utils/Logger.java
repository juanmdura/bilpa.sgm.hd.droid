package com.bilpa.android.utils;

import android.util.Log;

import com.bilpa.android.BuildConfig;

public class Logger {

    public static final boolean ENABLED = BuildConfig.LOG_HTTP_REQUESTS;

    private static String getTAG(String tag) {
        return "bilpa/"+tag;
    }

    public static void e(String tag, String msg) {
        if (ENABLED) {
            Log.e(getTAG(tag), msg);
        }
    }

    public static void e(String tag, String msg, Exception e) {
        if (ENABLED) {
            Log.e(getTAG(tag), msg, e);
        }
    }

    public static void w(String tag, String msg) {
        if (ENABLED) {
            Log.w(getTAG(tag), msg);
        }
    }

    public static void i(String tag, String msg) {
        if (ENABLED) {
            Log.i(getTAG(tag), msg);
        }
    }

    public static void d(String tag, String msg) {
        if (ENABLED) {
            Log.d(getTAG(tag), msg);
        }
    }

    public static void v(String tag, String msg) {
        if (ENABLED) {
            Log.v(getTAG(tag), msg);
        }
    }


}
