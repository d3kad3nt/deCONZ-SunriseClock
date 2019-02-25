package org.asdfgamer.sunriseClock.network.response;

import com.android.volley.VolleyError;

/**
 * This is the abstract class of all Responses from Deconz. It is specialised in {@link DeconzResponseArray} and {@link DeconzResponseObject}.
 */
public abstract class DeconzResponse {


    /**
     * The http return code of the request. If the request couldn't be started it is '-1'.
     */
    private int statuscode = -1;

    /**
     * This indicates if volly thinks that an error occurred.
     */
    private boolean success;

    /**
     * This contains the VolleyError, if an error occured.
     */
    private VolleyError error = null;

    public int getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(int statuscode) {
        this.statuscode = statuscode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public VolleyError getError() {
        return error;
    }

    public void setError(VolleyError error) {
        this.error = error;
    }
}
