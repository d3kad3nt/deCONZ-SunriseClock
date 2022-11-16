package org.d3kad3nt.sunriseClock.data.remote.common;

import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import org.d3kad3nt.sunriseClock.data.model.endpoint.BaseEndpoint;
import org.jetbrains.annotations.NotNull;

public abstract class NetworkUpdateResource<UpdateType, ResourceType> extends MediatorLiveData<EmptyResource> {


    protected ResourceType resource;

    public NetworkUpdateResource(LiveData<BaseEndpoint> endpointLiveData) {
        setValue(EmptyResource.loading(""));
        LiveData<ResourceType> resourceLoad = loadResourceFromDB();
        if (resourceLoad != null){
            addSource(resourceLoad, resource -> resourceLoadObserver(resource, resourceLoad, endpointLiveData));
        }else {
            addSource(endpointLiveData, baseEndpoint -> endpointLiveDataObserver(baseEndpoint, endpointLiveData));
        }
    }

    protected LiveData<ResourceType> loadResourceFromDB() {
        return null;
    }

    @NotNull
    protected abstract LiveData<ApiResponse<UpdateType>> sendNetworkRequest(BaseEndpoint baseEndpoint);

    @NotNull
    protected abstract LiveData<Resource<ResourceType>> updateResource();

    private void resourceLoadObserver(ResourceType resource, LiveData<ResourceType> resourceLiveData, LiveData<BaseEndpoint> endpointLiveData){
        if (resource == null) {
            updateValue(EmptyResource.loading("Resource loads"));
        }else{
            this.resource = resource;
            addSource(endpointLiveData, endpoint -> endpointLiveDataObserver(endpoint, endpointLiveData));
            removeSource(resourceLiveData);
        }
    }

    private void endpointLiveDataObserver(BaseEndpoint baseEndpoint, LiveData<BaseEndpoint> endpointLiveData) {
        if (baseEndpoint == null) {
            updateValue(EmptyResource.loading("Endpoints loads"));
        } else {
            LiveData<ApiResponse<UpdateType>> networkResponseLivedata = this.sendNetworkRequest(baseEndpoint);
            addSource(networkResponseLivedata, response -> networkResponseObserver(response, networkResponseLivedata));
            removeSource(endpointLiveData);
        }
    }

    private void networkResponseObserver(ApiResponse<UpdateType> response, LiveData<ApiResponse<UpdateType>> networkResponseLivedata) {
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
