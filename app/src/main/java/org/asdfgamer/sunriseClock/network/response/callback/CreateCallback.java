package org.asdfgamer.sunriseClock.network.response.callback;

import org.asdfgamer.sunriseClock.network.response.model.Error;

/**
 * Callback interface for deconz network requests returning the following HTTP status codes:
 * HTTP status code 400 and the ones from {@see BaseCallback}.
 * <p>
 * This is often returned for requests CREATING a resource, hence the name.
 *
 * @param <T>
 */
public interface CreateCallback<T> extends BaseCallback<T> {

    /**
     * Called for all HTTP status codes representing a bad (eg. malformed) request:
     * HTTP status code 400.
     *
     * @param error A custom error object describing the error that occurred in deconz.
     */
    void onBadRequest(Error error);

}
