package org.asdfgamer.sunriseClock.network.listener;

import org.asdfgamer.sunriseClock.network.response.DeconzResponse;

/**
 * This listener is used to communicate between the network packet and the gui.
 */
public abstract class GUIListener {

    /**
     * this methode gets called after the network response has been interpreted. It decides weather the {@code GUIListener#successOutput} or {@code GUIListener#errorOutput} should be called.
     *
     * @param response The response that gets interpreted.
     */
    void callback(DeconzResponse response) {
        if (response.isSuccess()) {
            successOutput(response);
        }
        else {
            errorOutput(response);
        }
    }

    /**
     * This gets called if the network call was a success.
     *
     * @param response the interpreted response
     */
    public abstract void successOutput(DeconzResponse response);

    /**
     * This gets called if the network call resulted in an error.
     *
     * @param response the interpreted response
     */
    public abstract void errorOutput(DeconzResponse response);

}
