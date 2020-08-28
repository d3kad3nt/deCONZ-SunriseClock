package org.d3kad3nt.sunriseClock.model.light;

public interface ILightRemoteColorable extends ICapability{

    int getColor();

    void requestSetColor(int color);
}
