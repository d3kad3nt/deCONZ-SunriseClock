package org.asdfgamer.sunriseClock.model.light;

public interface ILightRemoteColorable {

    boolean COLORABLE = true;



    int getColor();

    void requestSetColor(int color);
}
