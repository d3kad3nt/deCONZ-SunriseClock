package org.d3kad3nt.sunriseClock.data.model.light;

import androidx.annotation.IntRange;

public class DbLightBuilder {

    private static final String TAG = "DbLightBuilder";

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
     * Builder for constructing DbLights.
     */
    public DbLightBuilder() {

    }

    public DbLight build() {
        // Validator logic is defined inside the constructor of DbLight (separation of concern).
        return new DbLight(endpointId, endpointLightId, name, isSwitchable, isOn, isDimmable, brightness,
            isTemperaturable, colorTemperature, isColorable, color, isReachable);
    }

    /**
     * @param endpointId Foreign key (Room/SQLite) of the remote endpoint that this light belongs to. Only one
     *                   endpoint light id (specific for that endpoint!) can exist for a single endpoint.
     */
    public DbLightBuilder setEndpointId(long endpointId) {
        this.endpointId = endpointId;
        return this;
    }

    /**
     * This field enables the remote endpoint to identify the correct light. A remote endpoint cannot work with the
     * lightId.
     *
     * @param endpointLightId Id for this light inside (!) the remote endpoint.
     */
    public DbLightBuilder setEndpointLightId(String endpointLightId) {
        this.endpointLightId = endpointLightId;
        return this;
    }

    /**
     * @param name Name that can be used by the user to identify this light.
     */
    public DbLightBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @param isSwitchable Whether the light's capabilities allow it to be turned on and off (true if allowed by the
     *                     device, false if not).
     */
    public DbLightBuilder setIsSwitchable(boolean isSwitchable) {
        this.isSwitchable = isSwitchable;
        return this;
    }

    /**
     * @param isOn Whether the light is currently switched on (true) or off (false).
     */
    public DbLightBuilder setIsOn(boolean isOn) {
        this.isOn = isOn;
        return this;
    }

    /**
     * @param isDimmable Whether the light's capabilities allow it to be dimmed (true if allowed by the device, false
     *                   if not).
     */
    public DbLightBuilder setIsDimmable(boolean isDimmable) {
        this.isDimmable = isDimmable;
        return this;
    }

    /**
     * @param brightness The current brightness of the light, where 0 is the lowest brightness or off (depending on
     *                   the light) and 100 is the highest brightness.
     */
    public DbLightBuilder setBrightness(@IntRange(from = 0, to = 100) int brightness) {
        this.brightness = brightness;
        return this;
    }

    /**
     * @param isTemperaturable Whether the light's capabilities allow its color temperature to be changed (true if
     *                         allowed by the device, false if not).
     */
    public DbLightBuilder setIsTemperaturable(boolean isTemperaturable) {
        this.isTemperaturable = isTemperaturable;
        return this;
    }

    // Todo: Add javadoc and @IntRange to document allowed values for the color temperature.
    public DbLightBuilder setColorTemperature(int colorTemperature) {
        this.colorTemperature = colorTemperature;
        return this;
    }

    /**
     * @param isColorable Whether the light's capabilities allow its color to be changed (true if allowed by the
     *                    device, false if not).
     */
    public DbLightBuilder setIsColorable(boolean isColorable) {
        this.isColorable = isColorable;
        return this;
    }

    // Todo: Add javadoc and @IntRange to document allowed values for the color.
    public DbLightBuilder setColor(int color) {
        this.color = color;
        return this;
    }

    public DbLightBuilder setIsReachable(boolean isReachable) {
        this.isReachable = isReachable;
        return this;
    }
}