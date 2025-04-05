package org.d3kad3nt.sunriseClock.util.serviceLocator;

import java.util.HashMap;
import java.util.Map;

abstract class Cache<K, T> {

    private final Map<K, T> instances = new HashMap<>();

    public T getInstance(K instanceName) {
        return instances.get(instanceName);
    }

    public void addInstance(K key, T instance) {
        if (instances.containsKey(key)) {
            throw new IllegalArgumentException("Can't override instance for " + key.toString());
        }
        instances.put(key, instance);
    }
}
