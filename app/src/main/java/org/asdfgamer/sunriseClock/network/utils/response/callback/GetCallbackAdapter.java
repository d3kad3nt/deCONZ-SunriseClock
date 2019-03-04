package org.asdfgamer.sunriseClock.network.utils.response.callback;

import org.asdfgamer.sunriseClock.network.utils.response.model.Error;

/**
 * Callback interface adapter for deconz network requests returning the HTTP status codes from
 * {@see GetCallback}.
 *
 * @param <T>
 */
public class GetCallbackAdapter<T> extends BaseCallbackAdapter<T> {
    private GetCallback<T> callback;

    private static final String TAG = "GetCallbackAdapter";

    public GetCallbackAdapter(GetCallback<T> callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    void handleResourceNotFound(Error error) {
        callback.onEverytime();
        callback.onRessourceNotFound(error);
    }
}
