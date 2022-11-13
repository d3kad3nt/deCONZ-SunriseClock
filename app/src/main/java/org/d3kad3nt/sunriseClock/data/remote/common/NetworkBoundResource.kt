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

package org.d3kad3nt.sunriseClock.data.remote.common

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import org.d3kad3nt.sunriseClock.serviceLocator.ExecutorType
import org.d3kad3nt.sunriseClock.serviceLocator.ServiceLocator

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 * Copied from the official Google architecture-components github-sample under https://github.com/android/architecture-components-samples/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/repository/NetworkBoundResource.kt
 *
 * You can read more about it in the [Architecture Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
 */
abstract class NetworkBoundResource<ResultType, RequestType> @MainThread constructor() : MediatorLiveData<Resource<ResultType>>()
{

    @MainThread
    private fun updateValue(newValue: Resource<ResultType>) {
        if (this.value != newValue) {
            this.value = newValue
        }
    }

    init {
        this.value = Resource.loading(null)
        val dbSource = loadFromDb()
        addSource(dbSource, fun(data: ResultType) {
            removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                addSource(dbSource) { newData ->
                    updateValue(Resource.success(newData))
                }
            }
        })
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        addSource(dbSource) { newData ->
            updateValue(Resource.loading(newData))
        }
        addSource(apiResponse) { response ->
            removeSource(apiResponse)
            removeSource(dbSource)
            when (response) {
                is ApiSuccessResponse -> {
                    ServiceLocator.getExecutor(ExecutorType.IO).execute {
                        saveCallResult(processResponse(response))
                        ServiceLocator.getExecutor(ExecutorType.MainThread).execute {
                            // we specially request a new live data,
                            // otherwise we will get immediately last cached value,
                            // which may not be updated with latest results received from network.
                            addSource(loadFromDb()) { newData ->
                                updateValue(Resource.success(newData))
                            }
                        }
                    }
                }
                is ApiEmptyResponse -> {
                    ServiceLocator.getExecutor(ExecutorType.MainThread).execute {
                        // reload from disk whatever we had
                        addSource(loadFromDb()) { newData ->
                            updateValue(Resource.success(newData))
                        }
                    }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    addSource(dbSource) { newData ->
                        updateValue(Resource.error(response.errorMessage, newData))
                    }
                }
            }
        }
    }

    protected open fun onFetchFailed() {}


    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body //TODO: Ugly, tailored for retrofit

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}