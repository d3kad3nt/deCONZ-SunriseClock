package org.d3kad3nt.sunriseClock.serviceLocator;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

abstract class Cache<K,T> {

    private static final String TAG = "Cache";

    private Map<K,T> instances = new HashMap<>();

    public T getInstance(K instanceName){
        return instances.get(instanceName);
    }

    public boolean addInstance(K key, T instance){
        if (instances.containsKey(key)){
            Log.d(TAG, "addService: ");
            return false;
        }
        instances.put(key,instance);
        return true;
    }

}
