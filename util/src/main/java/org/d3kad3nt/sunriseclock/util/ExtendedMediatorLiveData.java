/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
 */

package org.d3kad3nt.sunriseclock.util;

import androidx.lifecycle.MediatorLiveData;

public class ExtendedMediatorLiveData<T> extends MediatorLiveData<T> {

    public void updateValue(T newValue) {
        if (this.getValue() != newValue) {
            this.setValue(newValue);
        }
    }
}
