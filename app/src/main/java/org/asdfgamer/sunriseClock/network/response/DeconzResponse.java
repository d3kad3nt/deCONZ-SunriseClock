package org.asdfgamer.sunriseClock.network.response;

/**
 * This is the abstract class of all Responses from Deconz. It is specialised in {@link DeconzResponseArray} and {@link DeconzResponseObject}.
 */
public abstract class DeconzResponse {


    /**
     * The http return code of the request.
     */
    private int returncode = 0;

    /**
     * This indicates if volly thinks that an error occured.
     */
    private boolean error;


    public int getReturncode() {
        return returncode;
    }

    public void setReturncode(int returncode) {
        this.returncode = returncode;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
