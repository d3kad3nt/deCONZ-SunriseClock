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

package org.d3kad3nt.sunriseClock.data.repository;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.data.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiEmptyResponse;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiErrorResponse;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiResponse;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiSuccessResponse;
import org.d3kad3nt.sunriseClock.serviceLocator.ExecutorType;
import org.d3kad3nt.sunriseClock.serviceLocator.ServiceLocator;
import org.d3kad3nt.sunriseClock.util.ExtendedMediatorLiveData;

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network. Copied from the
 * official Google architecture-components github-sample under https://github
 * .com/android/architecture-components-samples/blob/master/GithubBrowserSample/app/src/main/java/com/android
 * /example/github/repository/NetworkBoundResource.kt
 * <p>
 * You can read more about it in the [Architecture Guide](https://developer.android.com/arch).
 */
public abstract class NetworkBoundResource <ResultType, RemoteType, DbType>
    extends ExtendedMediatorLiveData<Resource<ResultType>> {

    private final LiveData<DbType> dbSource;
    protected BaseEndpoint endpoint = null;
    protected DbType dbObject = null;

    public NetworkBoundResource() {
        this.setValue(Resource.loading(null));

        dbSource = loadFromDb();
        this.addSource(dbSource, dbObject -> {
            dbSourceObserver(dbObject, dbSource);
        });
    }

    private void dbSourceObserver(DbType data, LiveData<DbType> dbSourceLiveData) {
        if (data == null) {
            updateValue(Resource.loading(null));
        } else {
            dbObject = data;
            removeSource(dbSourceLiveData);
            if (shouldFetch(dbObject)) {
                LiveData<BaseEndpoint> endpointLiveData = loadEndpoint();
                addSource(endpointLiveData, baseEndpoint -> {
                    endpointLiveDataObserver(baseEndpoint, endpointLiveData);
                });
            } else {
                addSource(dbSource, newData -> {
                    updateValue(Resource.success(convertDbTypeToResultType(newData)));
                });
            }
        }
    }

    private void endpointLiveDataObserver(BaseEndpoint endpoint, LiveData<BaseEndpoint> endpointLiveData) {
        if (endpoint == null) {
            updateValue(Resource.loading(null));
        } else {
            this.endpoint = endpoint;
            fetchFromNetwork(dbSource);
            removeSource(endpointLiveData);
        }
    }

    private void fetchFromNetwork(LiveData<DbType> dbSource) {
        LiveData<ApiResponse<RemoteType>> apiResponse = loadFromNetwork();
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        addSource(dbSource, newData -> {
            updateValue(Resource.loading(convertDbTypeToResultType(newData)));
        });
        addSource(apiResponse, response -> {
            removeSource(apiResponse);
            removeSource(dbSource);
            Class<? extends ApiResponse> aClass = response.getClass();
            if (ApiSuccessResponse.class.equals(aClass)) {
                ServiceLocator.getExecutor(ExecutorType.IO).execute(() -> {
                    saveResponseToDb(convertRemoteTypeToDbType((ApiSuccessResponse<RemoteType>) response));
                    ServiceLocator.getExecutor(ExecutorType.MainThread).execute(() -> {
                        // we specially request a new live data,
                        // otherwise we will get immediately last cached value,
                        // which may not be updated with latest results received from
                        // network.
                        addSource(loadFromDb(), newData -> {
                            updateValue(Resource.success(convertDbTypeToResultType(newData)));
                        });
                    });
                });
            } else if (ApiEmptyResponse.class.equals(aClass)) {
                ServiceLocator.getExecutor(ExecutorType.MainThread).execute(() -> {
                    // reload from disk whatever we had
                    addSource(loadFromDb(), newData -> {
                        updateValue(Resource.success(convertDbTypeToResultType(newData)));
                    });
                });
            } else if (ApiErrorResponse.class.equals(aClass)) {
                onFetchFailed();
                addSource(dbSource, newData -> {
                    updateValue(Resource.error(((ApiErrorResponse<RemoteType>) response).getErrorMessage(),
                        convertDbTypeToResultType(newData)));
                });
            }
        });
    }

    protected void onFetchFailed() {
    }

    @WorkerThread
    protected abstract void saveResponseToDb(DbType item);

    @MainThread
    protected abstract boolean shouldFetch(DbType data);

    @MainThread
    protected abstract LiveData<BaseEndpoint> loadEndpoint();

    @MainThread
    protected abstract LiveData<DbType> loadFromDb();

    @MainThread
    protected abstract LiveData<ApiResponse<RemoteType>> loadFromNetwork();

    protected abstract ResultType convertDbTypeToResultType(DbType item);

    protected abstract DbType convertRemoteTypeToDbType(ApiSuccessResponse<RemoteType> response);
}