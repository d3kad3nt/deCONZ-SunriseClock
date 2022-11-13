package org.d3kad3nt.sunriseClock.data.remote.common;

import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import org.d3kad3nt.sunriseClock.data.model.endpoint.BaseEndpoint;
import org.jetbrains.annotations.NotNull;

public abstract class NetworkUpdateResource<UpdateType, ResourceType> extends MediatorLiveData<EmptyResource> {

    private final LiveData<BaseEndpoint> endpointLiveData;

    private LiveData<ApiResponse<UpdateType>> networkResponseLivedata;

    public NetworkUpdateResource(LiveData<BaseEndpoint> endpointLiveData){
        this.endpointLiveData = endpointLiveData;
        setValue(EmptyResource.loading(""));
        addSource(endpointLiveData, baseEndpoint -> endpointLiveDataObserver(baseEndpoint) );
    }

    @NotNull
    protected abstract LiveData<ApiResponse<UpdateType>> sendNetworkRequest(BaseEndpoint baseEndpoint);

    @NotNull
    protected abstract LiveData<Resource<ResourceType>> updateResource();

    private void endpointLiveDataObserver(BaseEndpoint baseEndpoint){
        if (baseEndpoint == null){
            updateValue(EmptyResource.loading("Endpoints loads"));
        }else{
            networkResponseLivedata = this.sendNetworkRequest(baseEndpoint);
            addSource(networkResponseLivedata,response -> networkResponseObserver(response) );
            removeSource(endpointLiveData);
        }
    }

    private void networkResponseObserver(ApiResponse<UpdateType> response){
        EmptyResource resource = toResource(response);
        if (resource.getStatus() != Status.SUCCESS) {
            updateValue(resource);
        }else {
            LiveData<Resource<ResourceType>> updateResponseLivedata =updateResource();
            addSource(updateResponseLivedata, updateResponse -> resourceUpdateObserver(updateResponse) );
            removeSource(networkResponseLivedata);
        }
    }

    private void resourceUpdateObserver(Resource<ResourceType> response){
        EmptyResource resource = EmptyResource.fromResource(response);
        updateValue(resource);
    }

    private <T> EmptyResource toResource(ApiResponse<T> response){
        if (response == null){
            return EmptyResource.loading("");
        }else{
            if (response instanceof ApiEmptyResponse || response instanceof ApiSuccessResponse){
                return EmptyResource.success("");
            }else{
                return EmptyResource.error( "");
            }
        }
    }

    @MainThread
    private void updateValue(EmptyResource newValue ) {
        if (getValue() != newValue) {
            setValue(newValue);
        }
    }

}
