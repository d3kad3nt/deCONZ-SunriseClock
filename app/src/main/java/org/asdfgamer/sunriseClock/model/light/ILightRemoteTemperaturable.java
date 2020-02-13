package org.asdfgamer.sunriseClock.model.light;

public interface ILightRemoteTemperaturable extends ICapability  {

    String FILTER = "Temperaturable = 1";

    int getColorTemperature();

    void requestSetColorTemperature(int colorTemperature);
}
