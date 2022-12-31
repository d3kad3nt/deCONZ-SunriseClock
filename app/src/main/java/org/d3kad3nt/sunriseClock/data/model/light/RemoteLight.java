package org.d3kad3nt.sunriseClock.data.model.light;

import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointType;

public class RemoteLight {
    private static final String TAG = "RemoteLight";

    private final EndpointType endpointType;

    private final long endpointId;
    private final String endpointLightId;

    private final String name;

    private final boolean isSwitchable;
    private final boolean isOn;

    private final boolean isDimmable;
    private final int brightness;

    private final boolean isTemperaturable;
    private final int colorTemperature;

    private final boolean isColorable;
    private final int color;

    RemoteLight(EndpointType endpointType, long endpointId, String endpointLightId, String name, boolean isSwitchable, boolean isOn, boolean isDimmable, int brightness, boolean isTemperaturable, int colorTemperature, boolean isColorable, int color) {
        this.endpointType = endpointType;
        this.endpointId = endpointId;
        this.endpointLightId = endpointLightId;
        this.name = name;
        this.isSwitchable = isSwitchable;
        this.isOn = isOn;
        this.isDimmable = isDimmable;
        this.brightness = brightness;
        this.isTemperaturable = isTemperaturable;
        this.colorTemperature = colorTemperature;
        this.isColorable = isColorable;
        this.color = color;
    }

    public EndpointType getEndpointType() {
        return endpointType;
    }

    public long getEndpointId() {
        return endpointId;
    }

    public String getEndpointLightId() {
        return endpointLightId;
    }

    public String getName() {
        return name;
    }

    public boolean getIsSwitchable() {
        return isSwitchable;
    }

    public boolean getIsOn() {
        return isOn;
    }

    public boolean getIsDimmable() {
        return isDimmable;
    }

    public int getBrightness() {
        return brightness;
    }

    public boolean getIsTemperaturable() {
        return isTemperaturable;
    }

    public int getColorTemperature() {
        return colorTemperature;
    }

    public boolean getIsColorable() {
        return isColorable;
    }

    public int getColor() {
        return color;
    }
}