package com.mikeriv.litu.signal;


import java.util.Arrays;

/**
 * Created by mlrivera on 2/12/17.
 */

public class SignalProcessingUtil {

  public enum BoundaryConditionType {
    USE_ZEROS,
    USE_REPEAT,
    DEFAULT;
    //USE_AVERAGE, // TODO add more boundary condition types
  }


  public static final BoundaryConditionType DEFAULT_BOUNDARY_CONDITION_TYPE =
      BoundaryConditionType.USE_REPEAT;

  public static BoundaryConditionType getDefaultBoundaryConditionType() {
    return DEFAULT_BOUNDARY_CONDITION_TYPE;
  }

  public static float[] applyDemeanAndMedianFilter(
      float[] values,
      int medianFilterWindowSize,
      BoundaryConditionType boundaryConditionType) {
    if (values == null) {
      throw new IllegalArgumentException("Values should not be null");
    }
    if (values.length <= 0) {
      throw new IllegalArgumentException(
          "Values must have length great than 0");
    }
    if (medianFilterWindowSize <= 0) {
      throw new IllegalArgumentException(
          "Window length must be greater than 0");
    }
    // Apply demeaning and then median filter
    return applyMedianFilter(
        subtractMean(values),
        medianFilterWindowSize,
        boundaryConditionType);
  }

  public static int getPeaksCount(
      float[] values,
      float epsilon,
      boolean shouldCountDownPeaks) {
    return getPeaksCount(values, 1, shouldCountDownPeaks);
  }

  public static int getPeaksCount(
      float[] values,
      float epsilon,
      int decreaseThreshold,
      boolean shouldCountDownPeaks) {
    if (values == null) {
      throw new IllegalArgumentException("Values should not be null");
    }
    if (values.length <= 0) {
      throw new IllegalArgumentException(
          "Values must have length great than 0");
    }
    int peaksCount = 0;
    float mean = getMean(values);
    for (int i = 0; i < values.length - 1; i++) {
      if (i == 0 || i == values.length - 1) {
        // TODO handle edge cases
        continue;
      }
      float prevValue = values[i - 1];
      float currentValue = values[i];
      float nextValue = values[i + 1];
      float prevDiff = currentValue - prevValue; // currentValue > preValue
      float nextDiff = nextValue - currentValue; // currentValue > nextValue
      if ((prevDiff >= epsilon) && (nextDiff >= epsilon)
          || (shouldCountDownPeaks
            && (prevValue >= currentValue && currentValue < nextValue))) {
        int counter = 1;
        int nextIndex = i + 2;
        for (int j = nextIndex;
             j < values.length && j <= nextIndex + decreaseThreshold;
             j++) {
          float runningValue = values[j];
          if (runningValue - currentValue >= epsilon) {
            break;
          }
          counter += 1;
        }
        if (counter >= decreaseThreshold) {
          peaksCount += 1;
        }
      }
    }
    return peaksCount;
  }

  public static int getZeroCrossingsCount(
      float[] values) {
    if (values == null) {
      throw new IllegalArgumentException("Values should not be null");
    }
    if (values.length <= 0) {
      throw new IllegalArgumentException(
          "Values must have length great than 0");
    }
    int zeroCrossingCount = 0;
    float lastValue = 0;
    for (float currentValue : values) {
      if ((lastValue >= 0 && currentValue < 0)
          || (lastValue <= 0 && currentValue > 0)) {
        zeroCrossingCount += 1;
      }
      lastValue = currentValue;
    }
    return zeroCrossingCount;
  }

  /**
   * Demeans a signal so that 0 is at the center; otherwise known as zeroing the signal
   *
   * @param values - the input signal to demena
   * @return
   */
  public static float[] subtractMean(float[] values) {
    if (values == null) {
      throw new IllegalArgumentException("Values should not be null");
    }
    if (values.length <= 0) {
      throw new IllegalArgumentException(
          "Values must have length great than 0");
    }
    float[] filteredValues = new float[values.length];
    float mean = getMean(values);
    for (int i = 0; i < values.length; i++) {
      filteredValues[i] = values[i] - mean;
    }
    return filteredValues;
  }

  /**
   * Applies a median filter with windowSize across the input values
   *
   * @param values       - the input signal to apply a median filter to
   * @param windowSize   - the size of the window to use for calculating the median
   * @param boundaryType - defines how to handle boundaries values in the array
   * @return the filtered values
   */
  public static float[] applyMedianFilter(
      float[] values,
      int windowSize,
      BoundaryConditionType boundaryType) {
    if (values == null) {
      throw new IllegalArgumentException("Values should not be null");
    }
    if (values.length <= 0) {
      throw new IllegalArgumentException(
          "Values must have length great than 0");
    }
    if (windowSize <= 0 && windowSize < values.length) {
      throw new IllegalArgumentException(
          "WindowSize must be >= 0 and less than number of values");
    }
    float[] filteredValues = new float[values.length];
    int halfWindowSize = windowSize / 2;
    for (int i = -halfWindowSize;
         i < values.length && i < (values.length - halfWindowSize);
         i++) {
      // Gets window with center of i + halfWindowSize
      float[] window = getWindowWithSize(
          values,
          i,
          windowSize,
          boundaryType);
      filteredValues[i + halfWindowSize] = getMedian(window);
    }
    return filteredValues;
  }

  /**
   * returns the median value in a float array
   *
   * @param values
   * @return
   */
  public static float getMedian(float[] values) {
    if (values == null) {
      throw new IllegalArgumentException("Values should not be null");
    }
    if (values.length <= 0) {
      throw new IllegalArgumentException(
          "Values must have length great than 0");
    }
    int size = values.length;
    int halfSize = size / 2;
    Arrays.sort(values);
    float median = 0;
    if (size % 2 == 0) {
      median = (values[halfSize] + values[halfSize - 1]) / 2f;
    } else {
      median = values[halfSize];
    }
    return median;
  }


  /**
   * Returns the average value from an input array
   *
   * @param values
   * @return
   */
  public static float getMean(float[] values) {
    if (values == null) {
      throw new IllegalArgumentException("Values should not be null");
    }
    if (values.length <= 0) {
      throw new IllegalArgumentException(
          "Values must have length great than 0");
    }
    float sum = 0;
    int numItems = values.length;
    for (float value : values) {
      sum += value;
    }
    return sum / numItems;
  }

  /**
   * Returns the average value from an input array
   *
   * @param values
   * @return
   */
  public static float getStdDeviation(float[] values) {
    if (values == null) {
      throw new IllegalArgumentException("Values should not be null");
    }
    if (values.length <= 0) {
      throw new IllegalArgumentException(
          "Values must have length great than 0");
    }
    float mean = getMean(values);
    float sum = 0;
    int numItems = values.length;
    for (float value : values) {
      float diff = value - mean;
      sum += Math.sqrt(diff * diff);
    }
    return sum / numItems;
  }

  /**
   * Returns a subarray of size windowSize from am input array Values centered at
   * i + halfWindowSize
   * Gets window with center of i + halfWindowSize
   *
   * @param values
   * @param startIndex
   * @param windowSize
   */
  public static float[] getWindowWithSize(
      float[] values,
      int startIndex,
      int windowSize,
      BoundaryConditionType boundaryType) {
    if (values == null) {
      throw new IllegalArgumentException("Values should not be null");
    }
    if (values.length <= 0) {
      throw new IllegalArgumentException(
          "Values must have length great than 0");
    }
    if (windowSize <= 0 && windowSize < values.length) {
      throw new IllegalArgumentException(
          "WindowSize must be >= 0 and less than number of values");
    }
    if (startIndex + windowSize / 2 < 0 || startIndex >= values.length) {
      throw new IllegalArgumentException(
          "StartIndex is out of range!");
    }
    float[] windowValues = new float[windowSize];
    for (int j = 0; j < windowSize; j++) {
      int index = startIndex + j;
      float value;
      if (index < 0) {
        // Uses repeat window style
        value = getBoundaryValue(values[0], boundaryType);
      } else if (index >= values.length) {
        value = getBoundaryValue(values[values.length - 1], boundaryType);
      } else {
        value = values[index];
      }
      windowValues[j] = value;
    }
    return windowValues;
  }

  /**
   * Returns a boundary value given a last known valid boundary and a boundary type
   *
   * @param value the valid boundary value (at the 0th or the size() -1 place)
   * @param type
   * @return
   */
  private static float getBoundaryValue(
      float value,
      BoundaryConditionType type) {
    if (type == BoundaryConditionType.DEFAULT) {
      type = getDefaultBoundaryConditionType();
    }
    switch (type) {
      case USE_ZEROS:
        return 0;
      case USE_REPEAT:
        return value;
      default:
        // TODO add other boundary types;
        throw new IllegalStateException("Unimplemented!");
    }
  }

}
