/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
 */

package org.d3kad3nt.sunriseClock.util;

import androidx.annotation.NonNull;

public class Empty {

    static Empty instance = new Empty();

    public static Empty getInstance() {
        return instance;
    }

    @NonNull
    @Override
    public String toString() {
        return "Empty{}";
    }
}
