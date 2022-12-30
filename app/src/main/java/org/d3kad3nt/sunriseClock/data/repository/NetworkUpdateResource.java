package org.d3kad3nt.sunriseClock.data.repository;

import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.data.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.data.model.resource.EmptyResource;
import org.d3kad3nt.sunriseClock.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.data.model.resource.Status;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiEmptyResponse;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiResponse;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiSuccessResponse;
import org.d3kad3nt.sunriseClock.util.ExtendedMediatorLiveData;
import org.jetbrains.annotations.NotNull;

public abstract class NetworkUpdateResource<UpdateType, ResourceType> extends ExtendedMediatorLiveData<EmptyResource> {

    protected ResourceType dbObject;

    public NetworkUpdateResource() {
        setValue(EmptyResource.loading(""));
        LiveData<ResourceType> resourceLoad = loadFromDB();
        addSource(resourceLoad, resource -> dbObjectLoadObserver(resource, resourceLoad));
    }

    protected abstract LiveData<ResourceType> loadFromDB();

    protected abstract LiveData<BaseEndpoint> loadEndpoint();

    @NotNull
    protected abstract LiveData<ApiResponse<UpdateType>> sendNetworkRequest(BaseEndpoint baseEndpoint);

    @NotNull
    protected abstract LiveData<Resource<ResourceType>> loadUpdatedVersion();

    private void dbObjectLoadObserver(ResourceType resource, LiveData<ResourceType> resourceLiveData){
        if (resource == null) {
            updateValue(EmptyResource.loading("Resource loads"));
        }else{
            this.dbObject = resource;
            LiveData<BaseEndpoint> endpointLiveData = loadEndpoint();
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
            LiveData<Resource<ResourceType>> updateResponseLivedata = loadUpdatedVersion();
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

}
