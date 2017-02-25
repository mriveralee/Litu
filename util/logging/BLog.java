package com.mikeriv.stepup.util;

import com.mikeriv.stepup.BuildConfig;

/**
 * Created by mlrivera on 2/4/17.
 */

public class BLog {

    public static final boolean SHOULD_DEBUG = BuildConfig.DEBUG;

    public static void i(String tag, String string) {
        if (!SHOULD_DEBUG || string == null) {
            return;∑
        }
        android.util.Log.i(tag, string);
    }

    public static void e(String tag, String string) {
        if (!SHOULD_DEBUG || string == null) {
            return;
        }
        android.util.Log.e(tag, string);
    }

    public static void d(String tag, String string) {
        if (!SHOULD_DEBUG || string == null) {
            return;
        }
        android.util.Log.d(tag, string);
    }

    public static void v(String tag, String string) {
        if (!SHOULD_DEBUG || string == null) {
            return;
        }
        android.util.Log.v(tag, string);
    }

    public static void w(String tag, String string) {
        if (tag == null || string == null) {
            return;
        }
        android.util.Log.w(tag, string);
    }

}
