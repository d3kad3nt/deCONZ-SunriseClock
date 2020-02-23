package org.asdfgamer.sunriseClock.model.light;

public interface ILightRemoteColorable extends ICapability{

    int getColor();

    void requestSetColor(int color);
}
