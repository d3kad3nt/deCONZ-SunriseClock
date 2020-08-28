package org.d3kad3nt.sunriseClock.model.light;

public interface ILightRemoteDimmable extends ICapability {

    int getBrightness();

    void requestSetBrightness(int brightness);
}
