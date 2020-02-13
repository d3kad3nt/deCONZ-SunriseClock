package org.asdfgamer.sunriseClock.model.light;

public interface ILightRemoteDimmable extends ICapability {

    String FILTER = "Dimmable = 1";

    int getBrightness();

    void requestSetBrightness(int brightness);
}
