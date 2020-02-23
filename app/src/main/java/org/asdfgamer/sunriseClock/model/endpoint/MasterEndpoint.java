package org.asdfgamer.sunriseClock.model.endpoint;

import org.asdfgamer.sunriseClock.model.LightEndpoint;
import org.asdfgamer.sunriseClock.model.light.BaseLight;
import org.asdfgamer.sunriseClock.model.light.BaseLightEndpoint;

//TODO: The MasterEndpoint has probably to be set manually for our BaseLight. MasterEndpoint
// and specific lights could be retrieved by a class like EndpointAndLights with the help of ROOM relations.
public class MasterEndpoint implements LightEndpoint {

    //TODO: Maybe a custom ROOM type converter could handle the creation of endpoint-specific objects for this?
    public transient BaseLightEndpoint baseLightEndpoint;

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
