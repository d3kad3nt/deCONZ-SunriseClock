package org.d3kad3nt.sunriseclock.data.remote.common;

public class ApiSuccessResponse<T> extends ApiResponse<T> {

    private final T body;

    public ApiSuccessResponse(T body) {
        this.body = body;
    }

    public T getBody() {
        return body;
    }
}