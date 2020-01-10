package org.asdfgamer.sunriseClock.model.light;

public interface ILightRemoteTemperaturable {

    boolean TEMPERATURABLE = true;

    int getColorTemperature();

    void requestSetColorTemperature(int colorTemperature);
}
