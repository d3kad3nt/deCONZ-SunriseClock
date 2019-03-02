package org.asdfgamer.sunriseClock.network.response.callback;

import org.asdfgamer.sunriseClock.network.response.model.Error;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Callback interface for deconz network requests returning the following HTTP status codes:
 * HTTP status codes {200, 201, 202, 304}, 403, 503.
 * <p>
 * Note that ALL deconz requests could return these status codes. Therefore it is used as
 * base-interface.
 * Attention: All network requests returning a HTTP response are considered a Success in retrofit
 * (and in this project). Network problems or retrofit configuration errors are handled in a separate
 * error callback {@see onRetrofitError}.
 *
 * @param <T>
 */
public interface BaseCallback<T> {

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

    /**
     * Called for all HTTP status codes representing an request which was not successful due to the server being too busy:
     * HTTP status code 503.
     * Seems to be currently unused by deconz: It is mentioned in the 'error handling' section in
     * the API docs, but is not returned by any response.
     */
    void onServiceUnavailable();

    /**
     * Called for ALL HTTP status codes.
     */
    void onEverytime();

    /**
     * Custom error: Returned if HTTP code suggested an error, but the given deconz response
     * could not be parsed as Error object {@see Error}. This could happen if the request was NOT sent to
     * a deconz endpoint, but rather to another (unrelated) webserver.
     */
    void onInvalidErrorObject();

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     * @see retrofit2.Callback
     */
    void onFailure(Call<T> call, Throwable throwable);

}
