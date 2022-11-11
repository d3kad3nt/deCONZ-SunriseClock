package org.d3kad3nt.sunriseClock.data.remote.common;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import org.d3kad3nt.sunriseClock.data.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.util.Empty;
import org.jetbrains.annotations.NotNull;

public abstract class NetworkUpdateResource<UpdateType, ResourceType> {

    private final LiveData<BaseEndpoint> endpointLiveData;

    private final MediatorLiveData<Resource<Empty>> resultLiveData;
    private LiveData<ApiResponse<UpdateType>> networkResponseLivedata;

    public NetworkUpdateResource(LiveData<BaseEndpoint> endpointLiveData){
        this.endpointLiveData = endpointLiveData;
        this.resultLiveData = new MediatorLiveData<>();
    }

    @NotNull
    protected abstract LiveData<ApiResponse<UpdateType>> sendNetworkRequest(BaseEndpoint baseEndpoint);

    @NotNull
    protected abstract LiveData<Resource<ResourceType>> updateResource();

    public LiveData<Resource<Empty>> asLiveData(){
        resultLiveData.addSource(endpointLiveData, baseEndpoint -> endpointLiveDataObserver(baseEndpoint) );
        return resultLiveData;
    }

    private void endpointLiveDataObserver(BaseEndpoint baseEndpoint){
        if (baseEndpoint == null){
            resultLiveData.setValue(new Resource<>(Status.LOADING, Empty.getInstance(), "Endpoints loads"));
        }else{
            networkResponseLivedata = this.sendNetworkRequest(baseEndpoint);
            resultLiveData.addSource(networkResponseLivedata,response -> networkResponseObserver(response) );
            resultLiveData.removeSource(endpointLiveData);
        }
    }

    private void networkResponseObserver(ApiResponse<UpdateType> response){
        Resource<Empty> resource = toResource(response);
        if (resource.getStatus() != Status.SUCCESS) {
            resultLiveData.setValue(resource);
        }else {
            LiveData<Resource<ResourceType>> updateResponseLivedata =updateResource();
            resultLiveData.addSource(updateResponseLivedata, updateResponse -> resourceUpdateObserver(updateResponse) );
            resultLiveData.removeSource(networkResponseLivedata);
        }
    }

    private void resourceUpdateObserver(Resource<ResourceType> response){
        Resource<Empty> resource = new Resource<>(response.getStatus(), Empty.getInstance(), response.getMessage());
        resultLiveData.setValue(resource);
    }

    private <T> Resource<Empty> toResource(ApiResponse<T> response){
        if (response == null){
            return new Resource<>(Status.LOADING, Empty.getInstance(), "");
        }else{
            if (response instanceof ApiEmptyResponse || response instanceof ApiSuccessResponse){
                return new Resource<>(Status.SUCCESS, Empty.getInstance(), "");
            }else{
                return new Resource<>(Status.ERROR, Empty.getInstance(), "");
            }
        }
    }

}
