package com.mikeriv.stepup.signal;

import com.google.common.base.Preconditions;

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

    public static int getPeaksCount(
            float[] values,
            int medianFilterWindowSize,
            BoundaryConditionType boundaryConditionType) {
        Preconditions.checkNotNull(values);
        Preconditions.checkArgument(values.length > 0 && medianFilterWindowSize > 0);
        // Apply demeaning
        float[] demeanValues = subtractMean(values);
        float[] medianFilteredValues = applyMedianFilter(
                demeanValues,
                medianFilterWindowSize,
                boundaryConditionType);
        int peaksCount = 0;
        float mean = getMean(medianFilteredValues);
        for (int i = 0; i < medianFilteredValues.length - 1; i++) {
            if (i == 0 || i == medianFilteredValues.length - 1) {
                // TODO handle edge cases
                continue;
            }
            float prevValue = medianFilteredValues[i - 1];
            float currentValue = medianFilteredValues[i];
            float nextValue = medianFilteredValues[i + 1];

            if ((prevValue < currentValue && currentValue > nextValue)
                    || (prevValue > currentValue && currentValue < nextValue)) {
                peaksCount += 1;
            }
        }
        return peaksCount;
    }

    public static int getZeroCrossingsCount(
            float[] values,
            int medianFilterWindowSize,
            BoundaryConditionType boundaryConditionType) {
        Preconditions.checkNotNull(values);
        Preconditions.checkArgument(values.length > 0 && medianFilterWindowSize > 0);
        // Apply demeaning
        float[] demeanValues = subtractMean(values);
        float[] medianFilteredValues = applyMedianFilter(
                demeanValues,
                medianFilterWindowSize,
                boundaryConditionType);
        int zeroCrossingCount = 0;
        float lastValue = 0;
        for (float currentValue : medianFilteredValues) {
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
     * @param values - the input signal to demena
     *
     * @return
     */
    public static float[] subtractMean(float[] values) {
        Preconditions.checkNotNull(values);
        Preconditions.checkArgument(values.length > 0);
        float[] filteredValues = new float[values.length];
        float mean = getMean(values);
        for (int i = 0; i < values.length; i++) {
            filteredValues[i] = values[i] - mean;
        }
        return filteredValues;
    }

    /**
     * Applies a median filter with windowSize across the input values
     * @param values - the input signal to apply a median filter to
     * @param windowSize - the size of the window to use for calculating the median
     * @param boundaryType - defines how to handle boundaries values in the array
     * @return the filtered values
     */
    public static float[] applyMedianFilter(
            float[] values,
            int windowSize,
            BoundaryConditionType boundaryType)  {
        Preconditions.checkNotNull(values);
        Preconditions.checkArgument(values.length > 0);
        Preconditions.checkArgument(windowSize > 0 && windowSize < values.length);
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
                    BoundaryConditionType.USE_REPEAT);
            filteredValues[i + halfWindowSize] = getMedian(window);
        }
        return filteredValues;
    }

    /**
     * returns the median value in a float array
     * @param values
     * @return
     */
    public static float getMedian(float[] values) {
        Preconditions.checkNotNull(values);
        Preconditions.checkArgument(values.length > 0);
        int size = values.length;
        int halfSize = size / 2;
        Arrays.sort(values);
        float median = 0;
        if (size % 2 == 0) {
            median = (values[halfSize] + values[halfSize - 1]) / 2f;
        } else {
            median  = values[halfSize];
        }
        return median;
    }


    /**
     * Returns the average value from an input array
     * @param values
     * @return
     */
    public static float getMean(float[] values) {
        Preconditions.checkNotNull(values);
        Preconditions.checkArgument(values.length > 0);
        float sum = 0;
        int numItems = values.length;
        for (float value : values) {
            sum += value;
        }
        return  sum / numItems;
    }

    /**
     * Returns a subarray of size windowSize from am input array Values centered at
     * i + halfWindowSize
     * Gets window with center of i + halfWindowSize
     * @param values
     * @param startIndex
     * @param windowSize
     */
    public static float[] getWindowWithSize(
            float[] values,
            int startIndex,
            int windowSize,
            BoundaryConditionType boundaryType) {
        Preconditions.checkNotNull(values);
        Preconditions.checkArgument(values.length > 0);
        Preconditions.checkArgument(windowSize >= 0);
        Preconditions.checkArgument(startIndex + windowSize / 2 >= 0 && startIndex < values.length);
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
     *  Returns a boundary value given a last known valid boundary and a boundary type
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
