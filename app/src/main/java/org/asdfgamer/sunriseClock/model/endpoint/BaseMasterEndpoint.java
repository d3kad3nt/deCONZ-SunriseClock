package org.asdfgamer.sunriseClock.model.endpoint;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.asdfgamer.sunriseClock.model.light.BaseLightEndpoint;

//TODO: The BaseMasterEndpoint has probably to be set manually for our BaseLight. BaseMasterEndpoint
// and specific lights could be retrieved by a class like EndpointAndLights with the help of ROOM relations.
@Entity
public class BaseMasterEndpoint {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String connectionString;

    public String addedAt;

    //TODO: Maybe a custom ROOM type converter could handle the creation of endpoint-specific objects for this?
    @Ignore
    public transient BaseLightEndpoint baseLightEndpoint;

}
