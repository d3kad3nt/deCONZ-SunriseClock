package org.asdfgamer.sunriseClock.model.light;

public interface ILightRemoteColorable extends ICapability {

    String FILTER = "COLORABLE = 1";

    int getColor();

    void requestSetColor(int color);
}
