package org.d3kad3nt.sunriseClock.data.model.light;

import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.util.Objects;

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

    private final boolean isReachable;

    private UILight(long lightId, long endpointId, String name, boolean isSwitchable, boolean isOn,
                    boolean isDimmable, @IntRange(from = 0, to = 100) int brightness, boolean isTemperaturable/*, int colorTemperature*/,
                    boolean isColorable/*, int color*/, boolean isReachable) {
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
        this.isReachable = isReachable;
    }

    @NonNull
    @Contract("_ -> new")
    public static UILight from(@NonNull DbLight dbLight) {
        // Place for conversion logic (if UI needs other data types or value ranges).
        UILight uiLight =
            new UILight(dbLight.getLightId(), dbLight.getEndpointId(), dbLight.getName(), dbLight.getIsSwitchable(),
                dbLight.getIsOn(), dbLight.getIsDimmable(), dbLight.getBrightness(), dbLight.getIsTemperaturable(),
                dbLight.getIsColorable(), dbLight.getIsReachable());
        Log.v(TAG,
            "Converted DbLight with lightId " + dbLight.getLightId() + " (endpointId " + dbLight.getEndpointId() +
                ", endpointLightId " + dbLight.getEndpointLightId() + ") to UILight.");
        return uiLight;
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

    @IntRange(from = 0, to = 100)
    public int getBrightness() {
        return brightness;
    }

    public boolean getIsTemperaturable() {
        return isTemperaturable;
    }

    public boolean getIsColorable() {
        return isColorable;
    }

    public boolean getIsReachable() {
        return isReachable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        UILight otherLight = (UILight) o;
        return Objects.equals(lightId, otherLight.lightId) && Objects.equals(endpointId, otherLight.endpointId) &&
            Objects.equals(name, otherLight.name) && Objects.equals(isSwitchable, otherLight.isSwitchable) &&
            Objects.equals(isOn, otherLight.isOn) && Objects.equals(isDimmable, otherLight.isDimmable) &&
            Objects.equals(brightness, otherLight.brightness) &&
            Objects.equals(isTemperaturable, otherLight.isTemperaturable) &&
            Objects.equals(isColorable, otherLight.isColorable) &&
            Objects.equals(isReachable, otherLight.isReachable);
    }
}