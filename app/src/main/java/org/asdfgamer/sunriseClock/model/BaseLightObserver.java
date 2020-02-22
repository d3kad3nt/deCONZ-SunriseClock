package org.asdfgamer.sunriseClock.model;

import android.util.Log;

import androidx.lifecycle.Observer;

import org.asdfgamer.sunriseClock.model.light.BaseLight;

public interface BaseLightObserver {

    void setOn(BaseLight light, boolean value);
}
