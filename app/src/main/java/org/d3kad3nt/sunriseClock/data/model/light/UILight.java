package org.d3kad3nt.sunriseClock.data.model.light;

import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public class UILight {

    private static final String TAG = "UiLight";

    private final long lightId;
    private final long endpointId;
    // endpointLightId not needed in UI

    private final String name;

    private final boolean isSwitchable;
    private final boolean isOn;

    private final boolean isDimmable;
    private final int brightness;

    private final boolean isTemperaturable;
    //private final int colorTemperature; // Not yet implemented in the backend

    private final boolean isColorable;
    //private final int color; // Not yet implemented in the backend

    private UILight(long lightId, long endpointId, String name, boolean isSwitchable, boolean isOn, boolean isDimmable, int brightness, boolean isTemperaturable/*, int colorTemperature*/, boolean isColorable/*, int color*/) {
        this.lightId = lightId;
        this.endpointId = endpointId;
        this.name = name;
        this.isSwitchable = isSwitchable;
        this.isOn = isOn;
        this.isDimmable = isDimmable;
        this.brightness = brightness;
        this.isTemperaturable = isTemperaturable;
        //this.colorTemperature = colorTemperature;
        this.isColorable = isColorable;
        //this.color = color;
    }

    public long getLightId() {
        return lightId;
    }

    public long getEndpointId() {
        return endpointId;
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

    public boolean getIsColorable() {
        return isColorable;
    }

    @NonNull
    @Contract("_ -> new")
    public static UILight from(@NonNull DbLight dbLight) {
        // Place for conversion logic (if UI needs other data types or value ranges).
        return new UILight(dbLight.getLightId(), dbLight.getEndpointId(), dbLight.getName(), dbLight.getIsSwitchable(), dbLight.getIsOn(), dbLight.getIsDimmable(), dbLight.getBrightness(), dbLight.getIsTemperaturable(), dbLight.getIsColorable());
    }
}