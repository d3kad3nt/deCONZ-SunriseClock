package org.asdfgamer.sunriseClock.serviceLocator;

class StringCache<T> extends Cache<String,T> {

    @Override
    public T getInstance(String instanceName){
        return super.getInstance(instanceName.toLowerCase());
    }
}
