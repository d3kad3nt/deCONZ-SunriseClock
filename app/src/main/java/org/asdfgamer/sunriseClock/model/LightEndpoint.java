package org.asdfgamer.sunriseClock.model;

import org.asdfgamer.sunriseClock.model.light.BaseLight;

public interface LightEndpoint {

    void requestSetOn(BaseLight light, boolean value);
    void requestSetColor(BaseLight light, int color);
    void requestSetBrightness(BaseLight light, int brightness);
    void requestSetColorTemperature(BaseLight light, int colortemp);
}
