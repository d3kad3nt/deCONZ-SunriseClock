package org.asdfgamer.sunriseClock.model.endpoint;

import android.util.Log;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.asdfgamer.sunriseClock.model.BaseLightObserver;
import org.asdfgamer.sunriseClock.model.light.BaseLight;
import org.asdfgamer.sunriseClock.model.light.BaseLightEndpoint;

//TODO: The BaseMasterEndpoint has probably to be set manually for our BaseLight. BaseMasterEndpoint
// and specific lights could be retrieved by a class like EndpointAndLights with the help of ROOM relations.
@Entity
public class BaseMasterEndpoint implements BaseLightObserver {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String connectionString;

    public String addedAt;

    //TODO: Maybe a custom ROOM type converter could handle the creation of endpoint-specific objects for this?
    @Ignore
    public transient BaseLightEndpoint baseLightEndpoint;

    @Override
    public void setOn(BaseLight light, boolean value) {
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
