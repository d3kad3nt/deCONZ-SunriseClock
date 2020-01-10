package org.asdfgamer.sunriseClock.model.light;

public interface ILightRemoteTemperaturable {

    int getColorTemperature();

    void requestSetColorTemperature(int colorTemperature);
}
