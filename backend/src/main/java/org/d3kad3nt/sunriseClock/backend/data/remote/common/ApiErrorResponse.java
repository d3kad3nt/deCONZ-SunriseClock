package org.d3kad3nt.sunriseClock.backend.data.remote.common;

public class ApiErrorResponse<T> extends ApiResponse<T> {

    private final String errorMessage;

    public ApiErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
