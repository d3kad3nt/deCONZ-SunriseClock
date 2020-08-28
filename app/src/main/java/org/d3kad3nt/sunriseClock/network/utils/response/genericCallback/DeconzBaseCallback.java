package org.d3kad3nt.sunriseClock.network.utils.response.genericCallback;

import org.d3kad3nt.sunriseClock.network.utils.response.custDeserializer.model.Error;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Callback interface for deconz network requests returning the following HTTP status codes:
 * HTTP status codes {200, 201, 202, 304}, 403.
 * <p>
 * Note that ALL deconz requests could return these status codes. Therefore it is used as
 * base-interface.
 * Attention: All network requests returning a HTTP response are considered a Success in retrofit
 * (and in this project). Network problems or retrofit configuration errors are handled in a separate
 * error callback {@see onRetrofitError}.
 *
 * @param <T>
 */
public interface DeconzBaseCallback<T> {

    /**
     * Called for all HTTP status codes representing a successful response:
     * HTTP status codes 200, 201, 202 and 304.
     *
     * @param response The classic response from retrofit, which can be used to obtain the deserialized object.
     */
    void onSuccess(Response<T> response);

    /**
     * Called for all HTTP status codes representing a forbidden request:
     * HTTP status code 403.
     *
     * @param error A custom error object describing the error that occurred in deconz.
     */
    void onForbidden(Error error);

    /* 'Special' callbacks: Not directly related to HTTP status codes.*/

    /**
     * Called regardless of the exact outcome of the request. Even if the request failed,
     * this should be triggered.
     */
    void onEverytime();

    /**
     * Custom error: Invoked when a network exception occurred talking to the server.
     *
     * @see java.io.IOException
     * @see retrofit2.Callback
     */
    void onNetworkFailure(Call<T> call, Throwable throwable);

    /**
     * Custom error: Invoked when an unexpected exception occurred deserializing the server
     * response.
     *
     * @see retrofit2.Callback
     */
    void onInvalidResponseObject(Call<T> call, Throwable throwable);

    /**
     * Custom error: Returned if HTTP code suggested an error, but the given deconz response
     * could not be parsed as Error object {@see Error}. This could happen if the request was NOT sent to
     * a deconz endpoint, but rather to another (unrelated) webserver.
     */
    void onInvalidErrorObject();

}
