package org.d3kad3nt.sunriseClock.data.remote.common;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import org.d3kad3nt.sunriseClock.data.model.endpoint.BaseEndpoint;
import org.jetbrains.annotations.NotNull;

public abstract class NetworkUpdateResource<UpdateType, ResourceType> {

    private final LiveData<BaseEndpoint> endpointLiveData;

    private final MediatorLiveData<EmptyResource> resultLiveData;
    private LiveData<ApiResponse<UpdateType>> networkResponseLivedata;

    public NetworkUpdateResource(LiveData<BaseEndpoint> endpointLiveData){
        this.endpointLiveData = endpointLiveData;
        this.resultLiveData = new MediatorLiveData<>();
    }

    @NotNull
    protected abstract LiveData<ApiResponse<UpdateType>> sendNetworkRequest(BaseEndpoint baseEndpoint);

    @NotNull
    protected abstract LiveData<Resource<ResourceType>> updateResource();

    public LiveData<EmptyResource> asLiveData(){
        resultLiveData.addSource(endpointLiveData, baseEndpoint -> endpointLiveDataObserver(baseEndpoint) );
        return resultLiveData;
    }

    private void endpointLiveDataObserver(BaseEndpoint baseEndpoint){
        if (baseEndpoint == null){
            resultLiveData.setValue(EmptyResource.loading("Endpoints loads"));
        }else{
            networkResponseLivedata = this.sendNetworkRequest(baseEndpoint);
            resultLiveData.addSource(networkResponseLivedata,response -> networkResponseObserver(response) );
            resultLiveData.removeSource(endpointLiveData);
        }
    }

    private void networkResponseObserver(ApiResponse<UpdateType> response){
        EmptyResource resource = toResource(response);
        if (resource.getStatus() != Status.SUCCESS) {
            resultLiveData.setValue(resource);
        }else {
            LiveData<Resource<ResourceType>> updateResponseLivedata =updateResource();
            resultLiveData.addSource(updateResponseLivedata, updateResponse -> resourceUpdateObserver(updateResponse) );
            resultLiveData.removeSource(networkResponseLivedata);
        }
    }

    private void resourceUpdateObserver(Resource<ResourceType> response){
        EmptyResource resource = EmptyResource.fromResource(response);
        resultLiveData.setValue(resource);
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
