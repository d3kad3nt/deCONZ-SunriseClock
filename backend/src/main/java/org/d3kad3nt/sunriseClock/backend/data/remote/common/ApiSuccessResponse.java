/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
 */

package org.d3kad3nt.sunriseClock.backend.data.remote.common;

public class ApiSuccessResponse<T> extends ApiResponse<T> {

    private final T body;

    public ApiSuccessResponse(T body) {
        this.body = body;
    }

    public T getBody() {
        return body;
    }
}
