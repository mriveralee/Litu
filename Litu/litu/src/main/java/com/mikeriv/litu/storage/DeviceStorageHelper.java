package com.mikeriv.litu.storage;

import android.os.Environment;

/**
 * Created by mlrivera on 12/4/16.
 */

public class DeviceStorageHelper {

    private static boolean sExternalStorageReadable;
    private static boolean sExternalStorageWritable;

    public static boolean isExternalStorageReadable() {
        checkStorage();
        return sExternalStorageReadable;
    }

    public static boolean isExternalStorageWritable() {
        checkStorage();
        return sExternalStorageWritable;
    }

    public static boolean isExternalStorageReadableAndWritable() {
        checkStorage();
        return sExternalStorageReadable && sExternalStorageWritable;
    }

    private static void checkStorage() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            sExternalStorageReadable = sExternalStorageWritable = true;
        } else if (state.equals(Environment.MEDIA_MOUNTED)
                || state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            sExternalStorageReadable = true;
            sExternalStorageWritable = false;
        } else {
            sExternalStorageReadable = sExternalStorageWritable = false;
        }
    }

}
