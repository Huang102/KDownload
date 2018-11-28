package com.kisen.kdownload.util;

import android.util.Log;

/**
 * description:
 * author: Kisenhuang
 * email: Kisenhuang@163.com
 * time: 2018/11/27 下午5:00
 */
public class KLog {

    private static boolean OPEN = false;
    private static String TAG = "kHttp";

    public static void configLog(boolean open) {
        OPEN = open;
    }

    public static void configLog(boolean open, String tag) {
        OPEN = open;
        TAG = "kHttp_" + tag;
    }

    public static void v(String msg) {
        if (OPEN) {
            Log.v(TAG, msg);
        }
    }

    public static void v(String msg, Throwable tr) {
        if (OPEN) {
            Log.v(TAG, msg, tr);
        }
    }

    public static void d(String msg) {
        if (OPEN) {
            Log.d(TAG, msg);
        }
    }


    public static void d(String msg, Throwable tr) {
        if (OPEN) {
            Log.d(TAG, msg, tr);
        }
    }

    public static void i(String msg) {
        if (OPEN) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String msg, Throwable tr) {
        if (OPEN) {
            Log.i(TAG, msg, tr);
        }
    }

    public static void w(String msg) {
        if (OPEN) {
            Log.w(TAG, msg);
        }
    }

    public static void w(String msg, Throwable tr) {
        if (OPEN) {
            Log.w(TAG, msg, tr);
        }
    }

    public static void e(String msg) {
        if (OPEN) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String msg, Throwable tr) {
        if (OPEN) {
            Log.e(TAG, msg, tr);
        }
    }

}
