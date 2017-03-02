package com.mikeriv.litu.arrays;

import java.util.ArrayList;

/**
 * Created by mlrivera on 2/12/17.
 */

public class ArrayUtil {

  public static float[] convertToFloatArray(double[] arr) {
    if (arr == null) {
      return null;
    }
    float[] output = new float[arr.length];
    int i = 0;
    for (double f : arr) {
      output[i] = (float) f;
      i += 1;
    }
    return output;
  }

  public static float[] convertToFloatArray(ArrayList<Float> arr) {
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

  public static double[] convertToDoubleArray(ArrayList<Double> arr) {
    if (arr == null) {
      return null;
    }
    double[] output = new double[arr.size()];
    int i = 0;
    for (Double f : arr) {
      output[i] = f;
      i += 1;
    }
    return output;
  }

}
