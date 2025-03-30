/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
 */

package org.d3kad3nt.sunriseClock.backend.data.remote.common;

import java.io.IOException;
import retrofit2.Response;

// TODO: Make this class more generic / usable for other endpoints (instead of only supporting
// retrofit's Response type). This could be achieved by shifting the logic to decide whether
// a request was successful or not (Type ApiSuccessResponse or ApiErrorResponse) to the specific
// endpoint. Deconz or MQTT endpoint should be able to implement this logic in their request or
// response processing logic!

/**
 * Common class used by API responses.
 *
 * <p>Adapted from the official Google architecture-components github-sample app under
 * https://github.com/android/architecture-components-samples/blob/master/GithubBrowserSample/app/src/main/java/com
 * /android/example/github/api/ApiResponse.kt.
 */
public abstract class ApiResponse<T> {

    public static <T> ApiResponse<T> create(Throwable error) {
        if (error.getMessage() != null) {
            return new ApiErrorResponse<>(error.getMessage());
        } else {
            return new ApiErrorResponse<>("Unknown error!");
        }
    }

    public static <T> ApiResponse<T> create(final Response<T> response) {
        if (response.isSuccessful()) {
            final T body = response.body();
            if (body == null || response.code() == 204) {
                return new ApiEmptyResponse<>();
            } else {
                return new ApiSuccessResponse<>(body);
            }
        } else {
            String msg = null;
            if (response.errorBody() != null) {
                try {
                    msg = response.errorBody().string();
                } catch (IOException ignored) {
                }
            }
            final String errorMsg;
            if ((msg == null) || msg.isEmpty()) {
                if (response.message().isEmpty()) {
                    errorMsg = "Unknown error!";
                } else {
                    errorMsg = response.message();
                }
            } else {
                errorMsg = msg;
            }
            return new ApiErrorResponse<>(errorMsg);
        }
    }
}
