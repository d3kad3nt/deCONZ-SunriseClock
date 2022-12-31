package org.d3kad3nt.sunriseClock.data.model.light;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public class UILight {

    private final String name;
    private final long id;
    private final boolean on;

    public UILight(String name, long id, boolean on) {
        this.name = name;
        this.id = id;
        this.on = on;
    }

    public int getColor() {
        return 0;
    }

    public int getBrightness() {
        return 0;
    }

    public boolean isOn() {
        return on;
    }

    public int getColorTemperature() {
        return 0;
    }

    public long getLightId() {
        return id;
    }

    public String getFriendlyName() {
        return name;
    }

    @NonNull
    @Contract("_ -> new")
    public static UILight from(@NonNull DbLight dbLight) {
        //Todo: Logic to convert db light to UI light
        return new UILight(dbLight.getName(), dbLight.getLightId(), dbLight.getIsOn());
    }
}