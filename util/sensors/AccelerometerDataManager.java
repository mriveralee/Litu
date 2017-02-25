package com.mikeriv.stepup.sensors;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;

/**
 * Created by mlrivera on 2/11/17.
 */

public class AccelerometerDataManager {

    private ArrayList<AccelerometerDataItem> mItems;

    public AccelerometerDataManager() {
        mItems = new ArrayList<>();
    }

    public AccelerometerDataManager(ArrayList<AccelerometerDataItem> items) {
        this();
        if (items == null) {
            return;
        }
        mItems.addAll(items);
    }


    public ArrayList<AccelerometerDataItem> getList() {
        return mItems;
    }

    public void addItem(AccelerometerDataItem item) {
        mItems.add(item);
    }

    public int getItemsSize() {
        return mItems.size();
    }

    public void clear() {
        mItems.clear();
    }

    public ArrayList<Float> getValuesX() {
        ArrayList<Float> orderedValues = new ArrayList<>();
        for (AccelerometerDataItem i : mItems) {
            orderedValues.add(i.getX());
        }
        return orderedValues;
    }

    public ArrayList<Float> getValuesY() {
        ArrayList<Float> orderedValues = new ArrayList<>();
        for (AccelerometerDataItem i : mItems) {
            orderedValues.add(i.getY());
        }
        return orderedValues;
    }

    public ArrayList<Float> getValuesZ() {
        ArrayList<Float> orderedValues = new ArrayList<>();
        for (AccelerometerDataItem i : mItems) {
            orderedValues.add(i.getZ());
        }
        return orderedValues;
    }

}
