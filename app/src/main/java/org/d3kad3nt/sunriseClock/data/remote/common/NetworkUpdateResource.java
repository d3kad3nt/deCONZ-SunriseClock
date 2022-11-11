package org.d3kad3nt.sunriseClock.data.remote.common;

import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.data.model.endpoint.BaseEndpoint;

public class NetworkUpdateResource {

    private final LiveData<BaseEndpoint> endpointLiveData;

    public NetworkUpdateResource(LiveData<BaseEndpoint> endpointLiveData){
        this.endpointLiveData = endpointLiveData;
    }



}
