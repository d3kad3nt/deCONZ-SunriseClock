package org.asdfgamer.sunriseClock.model.light;

public interface BaseLightEndpoint {

    void setOn(BaseLight light, boolean on);

    void setBrightness(BaseLight light, int brightness);

    void setColorTemperature(BaseLight light, int colorTemperature);

    void setColor(BaseLight light, int color);

}
