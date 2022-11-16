/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.d3kad3nt.sunriseClock.data.remote.common;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import org.d3kad3nt.sunriseClock.data.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.serviceLocator.ExecutorType;
import org.d3kad3nt.sunriseClock.serviceLocator.ServiceLocator;

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 * Copied from the official Google architecture-components github-sample under https://github.com/android/architecture-components-samples/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/repository/NetworkBoundResource.kt
 *
 * You can read more about it in the [Architecture Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
 */
public abstract class NetworkBoundResource<ResultType, RequestType> extends  MediatorLiveData<Resource<ResultType>>{

    protected BaseEndpoint endpoint = null;
    protected ResultType resource = null;

    @MainThread
    private void updateValue(Resource<ResultType> newValue) {
        if (this.getValue() != newValue) {
            this.setValue(newValue);
        }
    }

    public NetworkBoundResource() {
        this.setValue( Resource.loading(null));

        LiveData<ResultType> resourceLiveData = loadResource();
        if (resourceLiveData != null){
            addSource(resourceLiveData,  resource ->{resourceLiveDataObserver(resource, resourceLiveData);});
        }else{
            LiveData<BaseEndpoint> endpointLiveData = loadEndpoint();
            addSource(endpointLiveData, endpoint -> {endpointLiveDataObserver(endpoint, endpointLiveData);});
        }
    }

    private void resourceLiveDataObserver(ResultType resource,LiveData<ResultType> resourceLiveData){
        if (resource == null){
            updateValue(Resource.loading(null));
        }else{
            this.resource = resource;
            LiveData<BaseEndpoint> endpointLiveData = loadEndpoint();
            addSource(endpointLiveData,endpoint -> {endpointLiveDataObserver(endpoint, endpointLiveData);});
            removeSource(resourceLiveData);
        }
    }

    private void endpointLiveDataObserver( BaseEndpoint endpoint, LiveData<BaseEndpoint> endpointLiveData){
        if (endpoint == null){
            updateValue(Resource.loading(null));
        }else{
            this.endpoint = endpoint;
            LiveData<ResultType> dbSource = loadFromDb();
            addSource(dbSource, (ResultType result ) -> {dbSourceObserver(result, dbSource); });
            removeSource(endpointLiveData);
        }
    }

    private void dbSourceObserver(ResultType data, LiveData<ResultType> dbSourceLiveData){
        removeSource(dbSourceLiveData);
        if (shouldFetch(data)) {
            fetchFromNetwork(dbSourceLiveData);
        } else {
            addSource(dbSourceLiveData, newData ->{
                updateValue(Resource.success(newData));
            });
        }
    }

    private void fetchFromNetwork(LiveData<ResultType> dbSource ) {
        LiveData<ApiResponse<RequestType>> apiResponse = loadFromNetwork();
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        addSource(dbSource, newData ->{
            updateValue(Resource.loading(newData));
        });
        addSource(apiResponse, response -> {
            removeSource(apiResponse);
            removeSource(dbSource);
            Class<? extends ApiResponse> aClass = response.getClass();
            if (ApiSuccessResponse.class.equals(aClass)) {
                ServiceLocator.getExecutor(ExecutorType.IO).execute(() -> {
                    saveCallResult(processResponse((ApiSuccessResponse<RequestType>) response));
                    ServiceLocator.getExecutor(ExecutorType.MainThread).execute(() -> {
                        // we specially request a new live data,
                        // otherwise we will get immediately last cached value,
                        // which may not be updated with latest results received from network.
                        addSource(loadFromDb(), newData -> {
                            updateValue((Resource<ResultType>) Resource.success(newData));
                        });
                    });
                });
            } else if (ApiEmptyResponse.class.equals(aClass)) {
                ServiceLocator.getExecutor(ExecutorType.MainThread).execute(() -> {
                    // reload from disk whatever we had
                    addSource(loadFromDb(), newData -> {
                        updateValue(Resource.success(newData));
                    });
                });
            } else if (ApiErrorResponse.class.equals(aClass)) {
                onFetchFailed();
                addSource(dbSource, newData -> {
                    updateValue(Resource.error(((ApiErrorResponse<RequestType>) response).getErrorMessage(), newData));
                });
            }
        });
    }

    protected void onFetchFailed() {}

    @WorkerThread
    protected RequestType processResponse(ApiSuccessResponse<RequestType> response ){
        //TODO: Ugly, tailored for retrofit
        return response.getBody();
    }

    @WorkerThread
    protected abstract void saveCallResult(RequestType item );

    @MainThread
    protected abstract boolean shouldFetch(ResultType data);

    @MainThread
    protected LiveData<ResultType> loadResource(){
        return null;
    }

    @MainThread
    protected abstract LiveData<BaseEndpoint> loadEndpoint();

    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> loadFromNetwork();
}