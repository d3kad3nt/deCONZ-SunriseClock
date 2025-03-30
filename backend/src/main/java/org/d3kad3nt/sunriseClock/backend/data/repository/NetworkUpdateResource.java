/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
 */

package org.d3kad3nt.sunriseClock.backend.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import org.d3kad3nt.sunriseClock.backend.data.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.EmptyResource;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.Status;
import org.d3kad3nt.sunriseClock.backend.data.remote.common.ApiEmptyResponse;
import org.d3kad3nt.sunriseClock.backend.data.remote.common.ApiResponse;
import org.d3kad3nt.sunriseClock.backend.data.remote.common.ApiSuccessResponse;
import org.d3kad3nt.sunriseClock.util.ExtendedMediatorLiveData;
import org.jetbrains.annotations.NotNull;

public abstract class NetworkUpdateResource<ResultType, RemoteType, DbType>
        extends ExtendedMediatorLiveData<EmptyResource> {

    protected DbType dbObject;

    public NetworkUpdateResource() {
        init();
    }

    public NetworkUpdateResource(final boolean alwaysExecuted) {
        init();
        if (alwaysExecuted) {
            observeForever(new Observer<>() {
                @Override
                public void onChanged(final EmptyResource emptyResource) {
                    if (!emptyResource.getStatus().equals(Status.LOADING)) {
                        removeObserver(this);
                    }
                }
            });
        }
    }

    private void init() {
        setValue(EmptyResource.loading(""));
        LiveData<DbType> resourceLoad = loadFromDB();
        addSource(resourceLoad, resource -> {
            dbObjectLoadObserver(resource, resourceLoad);
        });
    }

    private void dbObjectLoadObserver(DbType resource, LiveData<DbType> resourceLiveData) {
        if (resource == null) {
            updateValue(EmptyResource.loading("Resource loads"));
        } else {
            this.dbObject = resource;
            LiveData<BaseEndpoint> endpointLiveData = loadEndpoint();
            addSource(endpointLiveData, endpoint -> {
                endpointLiveDataObserver(endpoint, endpointLiveData);
            });
            removeSource(resourceLiveData);
        }
    }

    private void endpointLiveDataObserver(BaseEndpoint baseEndpoint, LiveData<BaseEndpoint> endpointLiveData) {
        if (baseEndpoint == null) {
            updateValue(EmptyResource.loading("Endpoints loads"));
        } else {
            LiveData<ApiResponse<RemoteType>> networkResponseLivedata = this.sendNetworkRequest(baseEndpoint);
            addSource(networkResponseLivedata, response -> {
                networkResponseObserver(response, networkResponseLivedata);
            });
            removeSource(endpointLiveData);
        }
    }

    private void networkResponseObserver(
            ApiResponse<RemoteType> response, LiveData<ApiResponse<RemoteType>> networkResponseLivedata) {
        EmptyResource resource = toResource(response);
        if (resource.getStatus() != Status.SUCCESS) {
            updateValue(resource);
        } else {
            LiveData<Resource<ResultType>> updateResponseLivedata = loadUpdatedVersion();
            addSource(updateResponseLivedata, updateResponse -> {
                resourceUpdateObserver(updateResponse);
            });
            removeSource(networkResponseLivedata);
        }
    }

    private void resourceUpdateObserver(Resource<?> response) {
        EmptyResource resource = EmptyResource.fromResource(response);
        updateValue(resource);
    }

    private <T> EmptyResource toResource(ApiResponse<T> response) {
        if (response == null) {
            return EmptyResource.loading("");
        } else {
            if (response instanceof ApiEmptyResponse || response instanceof ApiSuccessResponse) {
                return EmptyResource.success("");
            } else {
                return EmptyResource.error("");
            }
        }
    }

    protected abstract LiveData<DbType> loadFromDB();

    protected abstract LiveData<BaseEndpoint> loadEndpoint();

    @NotNull
    protected abstract LiveData<ApiResponse<RemoteType>> sendNetworkRequest(BaseEndpoint baseEndpoint);

    @NotNull
    protected abstract LiveData<Resource<ResultType>> loadUpdatedVersion();
}
