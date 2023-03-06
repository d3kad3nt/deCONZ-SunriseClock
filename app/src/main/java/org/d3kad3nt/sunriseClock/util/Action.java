package org.d3kad3nt.sunriseClock.util;

@FunctionalInterface
public interface Action <T> {

    void execute(final T t);
}
