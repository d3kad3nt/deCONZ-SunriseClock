package org.d3kad3nt.sunriseClock.data.model.light;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public class UILight implements Light {

    private final String name;
    private final long id;

    public UILight(String name, long id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public int getColor() {
        return 0;
    }

    @Override
    public int getBrightness() {
        return 0;
    }

    @Override
    public boolean isOn() {
        return false;
    }

    @Override
    public int getColorTemperature() {
        return 0;
    }

    @Override
    public long getLightId() {
        return this.id;
    }

    @Override
    public String getFriendlyName() {
        return this.name;
    }

    @NonNull
    @Contract("_ -> new")
    public static UILight from(@NonNull BaseLight baseLight) {
        //Todo: Logic to convert db light to UI light
        return new UILight(baseLight.getFriendlyName(), baseLight.getLightId());
    }
}