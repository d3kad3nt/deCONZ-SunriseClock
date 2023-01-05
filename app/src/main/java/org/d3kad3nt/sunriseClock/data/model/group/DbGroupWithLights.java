package org.d3kad3nt.sunriseClock.data.model.group;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import org.d3kad3nt.sunriseClock.data.model.light.DbLight;

import java.util.List;

public class DbGroupWithLights {

    @Embedded
    public DbGroup dbGroup;

    @Relation(parentColumn = "group_id",
        entityColumn = "light_id",
        associateBy = @Junction(DbGroupLightCrossref.class))
    public List<DbLight> dbLights;
}
