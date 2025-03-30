/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
 */

package org.d3kad3nt.sunriseClock.backend.data.remote.deconz;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.d3kad3nt.sunriseClock.backend.data.remote.common.ApiResponse;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * Adapted from the official Google architecture-components github-sample app under
 * https://github.com/android/architecture-components-samples/blob/master/GithubBrowserSample/app/src/main/java/com
 * /android/example/github/util/LiveDataCallAdapterFactory.kt.
 */
class LiveDataCallAdapterFactory extends CallAdapter.Factory {

    @Override
    public CallAdapter<?, ?> get(
            @NonNull Type returnType, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        if (getRawType(returnType) != LiveData.class) {
            return null;
        }
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Class<?> rawObservableType = getRawType(observableType);
        if (rawObservableType != ApiResponse.class) {
            throw new IllegalArgumentException("Type must be a resource!");
        }
        if (!(observableType instanceof ParameterizedType)) {
            throw new IllegalArgumentException("Resource must be parameterized");
        }
        Type bodyType = getParameterUpperBound(0, (ParameterizedType) observableType);
        return new LiveDataCallAdapter<>(bodyType);
    }
}
