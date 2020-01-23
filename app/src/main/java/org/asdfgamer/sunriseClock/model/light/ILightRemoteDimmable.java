package org.asdfgamer.sunriseClock.model.light;

public interface ILightRemoteDimmable {

    boolean DIMMABLE = true;

    int getBrightness();

    void requestSetBrightness(int brightness);
}
