package org.asdfgamer.sunriseClock.model.light;

public interface ILightRemoteTemperaturable extends ICapability  {

    int getColorTemperature();

    void requestSetColorTemperature(int colorTemperature);
}
