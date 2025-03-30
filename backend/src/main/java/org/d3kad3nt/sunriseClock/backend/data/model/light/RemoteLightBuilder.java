/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
 */

package org.d3kad3nt.sunriseClock.backend.data.model.light;

import org.d3kad3nt.sunriseClock.backend.data.model.endpoint.EndpointType;

public class RemoteLightBuilder {

    private EndpointType endpointType;

    private long endpointId;
    private String endpointLightId;

    private String name = "NoName";

    private boolean isSwitchable = false;
    private boolean isOn = false;

    private boolean isDimmable = false;
    private int brightness = 0;

    private boolean isTemperaturable = false;
    private int colorTemperature = 0;

    private boolean isColorable = false;
    private int color = 0;

    private boolean isReachable = true;

    /** Builder for constructing RemoteLights. */
    public RemoteLightBuilder() {}

    public RemoteLight build() {
        if (endpointType == null) {
            throw new IllegalStateException("RemoteLightBuilder cannot build this light without an endpoint type! "
                    + "Check remote light" + " parsing logic.");
        }
        return new RemoteLight(
                endpointType,
                endpointId,
                endpointLightId,
                name,
                isSwitchable,
                isOn,
                isDimmable,
                brightness,
                isTemperaturable,
                colorTemperature,
                isColorable,
                color,
                isReachable);
    }

    public RemoteLightBuilder setEndpointType(EndpointType endpointType) {
        this.endpointType = endpointType;
        return this;
    }

    public RemoteLightBuilder setEndpointId(long endpointId) {
        this.endpointId = endpointId;
        return this;
    }

    public RemoteLightBuilder setEndpointLightId(String endpointLightId) {
        this.endpointLightId = endpointLightId;
        return this;
    }

    public RemoteLightBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public RemoteLightBuilder setIsSwitchable(boolean isSwitchable) {
        this.isSwitchable = isSwitchable;
        return this;
    }

    public RemoteLightBuilder setIsOn(boolean isOn) {
        this.isOn = isOn;
        return this;
    }

    public RemoteLightBuilder setIsDimmable(boolean isDimmable) {
        this.isDimmable = isDimmable;
        return this;
    }

    public RemoteLightBuilder setBrightness(int brightness) {
        this.brightness = brightness;
        return this;
    }

    public RemoteLightBuilder setIsTemperaturable(boolean isTemperaturable) {
        this.isTemperaturable = isTemperaturable;
        return this;
    }

    public RemoteLightBuilder setColorTemperature(int colorTemperature) {
        this.colorTemperature = colorTemperature;
        return this;
    }

    public RemoteLightBuilder setIsColorable(boolean isColorable) {
        this.isColorable = isColorable;
        return this;
    }

    public RemoteLightBuilder setColor(int color) {
        this.color = color;
        return this;
    }

    public RemoteLightBuilder setIsReachable(boolean isReachable) {
        this.isReachable = isReachable;
        return this;
    }
}
