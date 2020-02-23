package org.asdfgamer.sunriseClock.model.light;

public interface ILightRemoteDimmable extends ICapability {

    int getBrightness();

    void requestSetBrightness(int brightness);
}
