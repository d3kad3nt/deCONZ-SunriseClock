package org.d3kad3nt.sunriseClock.network.utils.response.genericCallback;

import retrofit2.Response;

/**
 * Simplified callback interface for deconz network requests, indicating EITHER success OR error.
 * This is useful in GUI usage where the exact reason for the failing request is not important.
 *
 * For times where this callback is too 'broad', an inner class can be used to extend the original
 * callback (and hide some callbacks).
 */
public interface SimplifiedCallback<T> {

    /**
     * Called for all HTTP status codes representing a successful response:
     * HTTP status codes 200, 201, 202 and 304.
     *
     * @param response The classic response from retrofit, which can be used to obtain the deserialized object.
     */
    void onSuccess(Response<T> response);

    /**
     * Called for all cases with non-success-indicating HTTP status codes OR other errors: If
     * {@see SimplifiedCallback.onSuccess} is not called, this callback will always be triggered.
     * Network errors, deserialization errors are other error types.
     */
    void onError();
}
