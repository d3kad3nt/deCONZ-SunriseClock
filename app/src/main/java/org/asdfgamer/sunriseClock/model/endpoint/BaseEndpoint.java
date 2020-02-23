package org.asdfgamer.sunriseClock.model.endpoint;

import org.asdfgamer.sunriseClock.model.LightEndpoint;
import org.asdfgamer.sunriseClock.model.light.BaseLight;

//TODO: The BaseEndpoint has probably to be set manually for our BaseLight.
public class BaseEndpoint implements LightEndpoint {

    @Override
    public void requestSetOn(BaseLight light, boolean value) {
        light.setOn(value);
    }

    @Override
    public void requestSetColor(BaseLight light, int color) {
        light.setColor(color);
    }

    @Override
    public void requestSetBrightness(BaseLight light, int brightness) {
        light.setBrightness(brightness);
    }

    @Override
    public void requestSetColorTemperature(BaseLight light, int colortemp) {
        light.setColorTemperature(colortemp);
    }
}
