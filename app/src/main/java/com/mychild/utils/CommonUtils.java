package com.mychild.utils;

import android.util.Log;

/**
 * Created by Sandeep on 17-03-2015.
 */
public class CommonUtils {
    private static final String TAG = "=====MyChild====";

    public static void getLogs(String str) {
        Log.i(TAG, "======" + str + "=====");
    }
}
