package org.d3kad3nt.sunriseClock.util;

import androidx.annotation.NonNull;

@FunctionalInterface
public interface Action <T> {

    void execute(@NonNull final T t);
}
