package org.d3kad3nt.sunriseClock.data.model.endpoint;

import org.d3kad3nt.sunriseClock.data.remote.deconz.DeconzEndpointBuilder;
import org.d3kad3nt.sunriseClock.data.remote.common.EndpointBuilder;

/**
 * Lists all currently implemented types of (remote) endpoints.
 */
public enum EndpointType {

    DECONZ(0, new DeconzEndpointBuilder(), 0, 255);

    private final int ID;

    private final EndpointBuilder BUILDER;

    private final int BRIGHTNESS_MIN;
    private final int BRIGHTNESS_MAX;

    EndpointType(int id, EndpointBuilder builder, int brightnessMin, int brightnessMax) {
        this.ID = id;
        this.BUILDER = builder;
        this.BRIGHTNESS_MIN = brightnessMin;
        this.BRIGHTNESS_MAX = brightnessMax;
    }

    public int getId() {
        return ID;
    }

    public EndpointBuilder getBuilder() {
        return BUILDER;
    }

    public int getMinBrightness() {
        return BRIGHTNESS_MIN;
    }

    public int getMaxBrightness() {
        return BRIGHTNESS_MAX;
    }
}
