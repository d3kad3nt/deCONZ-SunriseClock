package org.d3kad3nt.sunriseClock.data.model.groupWithLights;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import org.d3kad3nt.sunriseClock.data.model.group.DbGroup;
import org.d3kad3nt.sunriseClock.data.model.light.DbLight;

import java.util.List;
import java.util.Locale;

public class DbGroupWithLights {

    @Embedded
    public DbGroup dbGroup;

    @Relation(parentColumn = "id",
              entityColumn = "id",
              entity = DbLight.class,
              associateBy = @Junction(value = DbGroupLightCrossref.class, parentColumn = "group_id", entityColumn =
                  "light_id"))
    public List<DbLight> dbLights;

    public static DbGroupWithLights from(final DbGroup group, final List<DbLight> groupLights) {
        DbGroupWithLights result = new DbGroupWithLights();
        result.dbGroup = group;
        result.dbLights = groupLights;
        return result;
    }

    @NonNull
    public String toString() {
        if (dbLights == null) {
            return String.format(Locale.ENGLISH, "Group %s (%d) without lights", dbGroup.getName(), dbGroup.getId());
        }
        return String.format(Locale.ENGLISH, "Group: %s with %d lights", dbGroup.getName(), dbLights.size());
    }
}
