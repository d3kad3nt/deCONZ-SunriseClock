package org.asdfgamer.sunriseClock.model.endpoint;

import androidx.room.Embedded;
import androidx.room.Relation;

import org.asdfgamer.sunriseClock.model.light.BaseLight;

import java.util.List;

public class EndpointWithLights {

    @Embedded
    public EndpointConfig endpointConfig;

    @Relation(parentColumn = "endpointID",
            entityColumn = "lightID")
    public List<BaseLight> baseLights;
}
