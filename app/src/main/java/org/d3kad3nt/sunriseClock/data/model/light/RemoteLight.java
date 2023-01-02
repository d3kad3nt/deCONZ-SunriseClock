package org.d3kad3nt.sunriseClock.data.model.light;

import android.util.Log;

import androidx.annotation.NonNull;

import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointType;
import org.jetbrains.annotations.Contract;

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

    RemoteLight(EndpointType endpointType, long endpointId, String endpointLightId, String name, boolean isSwitchable,
                boolean isOn, boolean isDimmable, int brightness, boolean isTemperaturable, int colorTemperature,
                boolean isColorable, int color) {
        this.endpointType = endpointType;

        if (endpointId != 0L) {
            this.endpointId = endpointId;
        } else {
            Log.e(TAG, "The given endpointId cannot be 0!");
            throw new IllegalArgumentException("The given endpointId cannot be 0!");
        }

        if (endpointLightId != null && !endpointLightId.isEmpty()) {
            this.endpointLightId = endpointLightId;
        } else {
            Log.e(TAG, "The given endpointLightId string cannot be null or empty!");
            throw new IllegalArgumentException("The given endpointLightId string cannot be null or empty!");
        }

        this.name = name;
        this.isSwitchable = isSwitchable;
        this.isOn = isOn;
        this.isDimmable = isDimmable;

        if (brightness >= endpointType.getMinBrightness() && brightness <= endpointType.getMaxBrightness()) {
            this.brightness = brightness;
        } else {
            throw new IllegalArgumentException(
                "The given brightness of a light (endpoint type " + endpointType.name() + ") must be between " +
                endpointType.getMinBrightness() + " and " + endpointType.getMaxBrightness() + "!");
        }

        this.isTemperaturable = isTemperaturable;
        this.colorTemperature = colorTemperature; //Todo: Define which values are allowed for each endpoint type
        this.isColorable = isColorable;
        this.color = color; //Todo: Define which values are allowed for each endpoint type
    }

    @NonNull
    @Contract("_ -> new")
    static DbLight toDbLight(RemoteLight remoteLight) {
        Log.d(TAG, "Converting RemoteLight to DbLight...");
        DbLightBuilder dbLightBuilder = new DbLightBuilder();
        //Logic to convert remote light to db light depending on the endpoint type this light originated from.
        DbLight dbLight = dbLightBuilder.setEndpointId(remoteLight.getEndpointId())
            .setEndpointLightId(remoteLight.getEndpointLightId()).setName(remoteLight.getName())
            .setIsSwitchable(remoteLight.getIsSwitchable()).setIsOn(remoteLight.getIsOn())
            .setIsDimmable(remoteLight.getIsDimmable())
            // Convert brightness via linear conversion (copied from https://stackoverflow.com/questions/929103/convert-a-number-range-to-another-range-maintaining-ratio)
            .setBrightness(calculateBrightness(remoteLight)).setIsTemperaturable(remoteLight.getIsTemperaturable())
            .setColorTemperature(remoteLight.getColorTemperature()) //Todo: Implement
            // conversion
            .setIsColorable(remoteLight.getIsColorable())
            .setColor(remoteLight.getColor()) //Todo: Implement conversion
            .build();
        Log.d(TAG, "Converted RemoteLight with endpointId " + remoteLight.getEndpointId() + " and endpointLightId " +
                   remoteLight.getEndpointLightId() + " to DbLight.");
        return dbLight;
    }

    private static int calculateBrightness(RemoteLight remoteLight) {

        return (((remoteLight.getBrightness() - remoteLight.getEndpointType().getMinBrightness()) *
                 (DbLight.BRIGHTNESS_MAX - DbLight.BRIGHTNESS_MIN)) /
                (remoteLight.getEndpointType().getMaxBrightness() -
                 remoteLight.getEndpointType().getMinBrightness())) + DbLight.BRIGHTNESS_MIN;
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

    /**
     * When endpointType is DECONZ: The current brightness of the light, where 0 is the lowest brightness or off and
     * 255 is the highest brightness. Depending on the light type 0 might not mean visible off but minimum
     * brightness.
     *
     * @return Brightness of the light, value range and meaning of the value depend on the endpoint this light
     * originated from.
     */
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