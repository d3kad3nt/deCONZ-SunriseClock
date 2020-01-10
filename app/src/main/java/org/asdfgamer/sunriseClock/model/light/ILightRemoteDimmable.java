package org.asdfgamer.sunriseClock.model.light;

public interface ILightRemoteDimmable {

    int getBrightness();

    void requestSetBrightness(int brightness);
}
