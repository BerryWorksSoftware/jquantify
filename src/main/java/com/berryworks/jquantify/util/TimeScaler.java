package com.berryworks.jquantify.util;

public class TimeScaler {
    private final String mUnits;
    private final float mScale;

    public TimeScaler(float inBase) {
        if (inBase >= 120000) {
            mUnits = "minutes";
            mScale = 60000;
        } else if (inBase >= 2000) {
            mUnits = "seconds";
            mScale = 1000;
        } else {
            mUnits = "ms";
            mScale = 1;
        }
    }

    public String getUnits() {
        return mUnits;
    }

    public float scale(float inValue) {
        return inValue / mScale;
    }

}

