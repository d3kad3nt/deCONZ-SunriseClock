package org.d3kad3nt.sunriseClock.data.model.endpoint;

import org.d3kad3nt.sunriseClock.data.remote.deconz.DeconzEndpointBuilder;
import org.d3kad3nt.sunriseClock.data.remote.common.EndpointBuilder;

/**
 * Lists all currently implemented types of (remote) enpoints.
 */
public enum EndpointType {

    DECONZ(0, new DeconzEndpointBuilder());

    private int id;

    private EndpointBuilder builder;

    EndpointType(int id, EndpointBuilder builder) {
        this.id = id;
        this.builder = builder;
    }

    public int getId() {
        return this.id;
    }

    public EndpointBuilder getBuilder() {
        return builder;
    }
}
