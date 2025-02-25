package org.d3kad3nt.sunriseClock.data.model.light;

import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointType;

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

    /**
     * Builder for constructing RemoteLights.
     */
    public RemoteLightBuilder() {

    }

    /**
     * Builder for constructing RemoteLights, based on an already existing light object.
     *
     * @param light The starting point for a light to be modified by builder methods.
     */
    public RemoteLightBuilder(RemoteLight light) {
        this.endpointType = light.getEndpointType();
        this.endpointId = light.getEndpointId();
        this.endpointLightId = light.getEndpointLightId();
        this.name = light.getName();
        this.isSwitchable = light.getIsSwitchable();
        this.isOn = light.getIsOn();
        this.isDimmable = light.getIsDimmable();
        this.brightness = light.getBrightness();
        this.isTemperaturable = light.getIsTemperaturable();
        this.colorTemperature = light.getColorTemperature();
        this.isColorable = light.getIsColorable();
        this.color = light.getColor();
    }

    public RemoteLight build() {
        //Todo: Check if endpointId and endpointLightId are set, problem: RemoteLightListTypeAdapter has to set
        // these values after the single light was parsed by GSON
        if (endpointType == null) {
            throw new IllegalStateException(
                "RemoteLightBuilder cannot build this light without an endpoint type! " + "Check remote light" +
                    " parsing logic.");
        }
        return new RemoteLight(endpointType, endpointId, endpointLightId, name, isSwitchable, isOn, isDimmable,
            brightness, isTemperaturable, colorTemperature, isColorable, color, isReachable);
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