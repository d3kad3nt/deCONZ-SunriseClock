package org.asdfgamer.sunriseClock.model.endpoint;

/**
 * Lists all currently implemented types of (remote) enpoints.
 */
public enum EndpointType {

    DECONZ(0);

    private int id;

    EndpointType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
