package com.mikeriv.stepup.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.google.common.base.Preconditions;

/**
 * Created by mlrivera on 2/2/17.
 */
public class SensorController implements SensorEventListener {

    public static final int INVALID_SENSOR_TYPE = -1;
    public static final int DEFAULT_SENSOR_DELAY = SensorManager.SENSOR_DELAY_NORMAL;

    public interface SensorControllerListener {
        void onSensorData(int sensorType, float[] values, int accuracy, long timestamp);
    }

    private SensorControllerListener mListener;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    private int mSensorDelay;
    private int mSensorType = 0;
    private int mAccuracy = 0;


    public SensorController(Context ctx, int sensorType) {
        this(ctx, sensorType, DEFAULT_SENSOR_DELAY);
    }

    public SensorController(Context ctx, int sensorType, int sensorSampleRate) {
        Preconditions.checkNotNull(ctx);
        Preconditions.checkArgument(sensorSampleRate >= 0, "Invalid sample rate");

        mSensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        Preconditions.checkNotNull(mSensorManager, "Sensor Manager should not be null!");

        mSensor = mSensorManager.getDefaultSensor(sensorType);
        mSensorType = sensorType;
        mSensorDelay = sensorSampleRate;
    }

    public void setSensorListener(SensorControllerListener listener) {
        mListener = listener;
    }

    public void stop() {
        mSensorManager.unregisterListener(this);
    }

    public void start() {
        mSensorManager.registerListener(this, mSensor, mSensorDelay);
    }

    public int getType() {
        return mSensorType;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Preconditions.checkNotNull(event, "Sensor Event can't be null!");
        if (event.sensor == mSensor) {
            if (mListener != null) {
                mListener.onSensorData(
                        event.sensor != null ? event.sensor.getType() : INVALID_SENSOR_TYPE,
                        event.values,
                        event.accuracy,
                        event.timestamp);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor == null) {
            return;
        }
        if (sensor == mSensor) {
            mAccuracy = accuracy;
        }
    }

}
