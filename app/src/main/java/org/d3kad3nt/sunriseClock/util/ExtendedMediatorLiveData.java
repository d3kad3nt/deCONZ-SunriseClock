package org.d3kad3nt.sunriseClock.util;

import androidx.lifecycle.MediatorLiveData;

public abstract class ExtendedMediatorLiveData<T> extends MediatorLiveData<T> {

    protected void updateValue(T newValue) {
        if (this.getValue() != newValue) {
            this.setValue(newValue);
        }
    }

}
