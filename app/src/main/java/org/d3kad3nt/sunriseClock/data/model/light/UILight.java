package org.d3kad3nt.sunriseClock.data.model.light;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public class UILight implements Light {

    private final String name;
    private final long id;
    private final boolean on;

    public UILight(String name, long id, boolean on) {
        this.name = name;
        this.id = id;
        this.on = on;
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
        return on;
    }

    @Override
    public int getColorTemperature() {
        return 0;
    }

    @Override
    public long getLightId() {
        return id;
    }

    @Override
    public String getFriendlyName() {
        return name;
    }

    @NonNull
    @Contract("_ -> new")
    public static UILight from(@NonNull BaseLight baseLight) {
        //Todo: Logic to convert db light to UI light
        return new UILight(baseLight.getFriendlyName(), baseLight.getLightId(), baseLight.isOn());
    }
}