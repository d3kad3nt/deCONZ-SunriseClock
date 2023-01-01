package org.d3kad3nt.sunriseClock.util;

import androidx.lifecycle.MediatorLiveData;

public class ExtendedMediatorLiveData <T> extends MediatorLiveData<T> {

    public void updateValue(T newValue) {
        if (this.getValue() != newValue) {
            this.setValue(newValue);
        }
    }

}
