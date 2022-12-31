package org.d3kad3nt.sunriseClock.data.model.light;

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

    /**
     * When endpointType is DECONZ: The current brightness of the light, where 0 is the lowest brightness or off and 255 is the highest brightness. Depending on the light type 0 might not mean visible off but minimum brightness.
     *
     * @return Brightness of the light, value range and meaning of the value depend on the endpoint this light originated from.
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

    @NonNull
    @Contract("_ -> new")
    static DbLight toDbLight(RemoteLight remoteLight) {
        DbLightBuilder dbLightBuilder = new DbLightBuilder();
        //Logic to convert remote light to db light depending on the endpoint type this light originated from.
        switch(remoteLight.getEndpointType()) {
            case DECONZ:
                return dbLightBuilder.setEndpointId(remoteLight.getEndpointId())
                        .setEndpointLightId(remoteLight.getEndpointLightId())
                        .setName(remoteLight.getName())
                        .setIsSwitchable(remoteLight.getIsSwitchable())
                        .setIsOn(remoteLight.getIsOn())
                        .setIsDimmable(remoteLight.getIsDimmable())
                        .setBrightness(Math.round(((float)remoteLight.getBrightness()/255)*100))
                        .setIsTemperaturable(remoteLight.getIsTemperaturable())
                        .setColorTemperature(remoteLight.getColorTemperature()) //Todo: Implement conversion
                        .setIsColorable(remoteLight.getIsColorable())
                        .setColor(remoteLight.getColor()) //Todo: Implement conversion
                        .build();
            default:
                throw new IllegalArgumentException("RemoteLight toDbLight cannot handle this endpoint type. Endpoint type id was " + remoteLight.getEndpointType().getId());
        }
    }
}