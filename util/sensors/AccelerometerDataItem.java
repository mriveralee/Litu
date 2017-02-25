package com.mikeriv.stepup.sensors;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

/**
 * Created by mlrivera on 2/11/17.
 */

public class AccelerometerDataItem implements Parcelable {

    private float mX;
    private float mY;
    private float mZ;

    private long mTimestamp;
    private int mAccuracy;

    public AccelerometerDataItem(float x, float y, float z, long timestamp) {
        mX = x;
        mY = y;
        mZ = z;
        mTimestamp = timestamp;
        mAccuracy = 0;
    }

    public AccelerometerDataItem(float x, float y, float z, long timestamp, int accuracy) {
        mX = x;
        mY = y;
        mZ = z;
        mTimestamp = timestamp;
        mAccuracy = accuracy;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public float getZ() {
        return mZ;
    }

    public long getTimestamp() {
        return mTimestamp;
    }


    public int getAccuracy() {
        return mAccuracy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<AccelerometerDataItem> CREATOR
            = new Parcelable.Creator<AccelerometerDataItem>() {
        public AccelerometerDataItem createFromParcel(Parcel in) {
            return new AccelerometerDataItem(in);
        }

        public AccelerometerDataItem[] newArray(int size) {
            return new AccelerometerDataItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(mX);
        dest.writeFloat(mY);
        dest.writeFloat(mZ);
        dest.writeLong(mTimestamp);
        dest.writeInt(mAccuracy);
    }

    private AccelerometerDataItem(Parcel in) {
        mX = in.readFloat();
        mY = in.readFloat();
        mZ = in.readFloat();
        mTimestamp = in.readLong();
        mAccuracy = in.readInt();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "<%d (%f, %f, %f)>", mTimestamp, mX, mY, mZ);
    }
}
