package com.mikeriv.stepup.util;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by mlrivera on 2/12/17.
 */

public class ArrayUtil {

    public static float[] toFloatArray(ArrayList<Float> arr) {
        if (arr == null) {
            return null;
        }
        float[] output = new float[arr.size()];
        int i = 0;
        for (Float f : arr) {
            output[i] = f;
            i += 1;
        }
        return output;
    }
}
