/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
 */

package org.d3kad3nt.sunriseClock.util.serviceLocator;

import java.util.HashMap;
import java.util.Map;

abstract class Cache<K, T> {

    private final Map<K, T> instances = new HashMap<>();

    public T getInstance(K instanceName) {
        return instances.get(instanceName);
    }

    public boolean addInstance(K key, T instance) {
        if (instances.containsKey(key)) {
            return false;
        }
        instances.put(key, instance);
        return true;
    }
}
