package org.d3kad3nt.sunriseClock.backend.data.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import org.d3kad3nt.sunriseClock.backend.data.model.group.DbGroup;
import org.d3kad3nt.sunriseClock.backend.data.model.light.DbLight;

@Entity(
        tableName = DbGroupLightCrossref.TABLENAME,
        primaryKeys = {"group_id", "light_id"},
        indices = {@Index(value = {"group_id"}), @Index(value = "light_id")},
        foreignKeys = {
            @ForeignKey(
                    entity = DbGroup.class,
                    parentColumns = "id",
                    childColumns = "group_id",
                    onDelete = ForeignKey.CASCADE),
            @ForeignKey(
                    entity = DbLight.class,
                    parentColumns = "id",
                    childColumns = "light_id",
                    onDelete = ForeignKey.CASCADE)
        })
public class DbGroupLightCrossref {

    @Ignore
    public static final String TABLENAME = "light_grouping";

    @ColumnInfo(name = "group_id")
    public final long groupId;

    @ColumnInfo(name = "light_id")
    public final long lightId;

    public DbGroupLightCrossref(final long groupId, final long lightId) {
        this.groupId = groupId;
        this.lightId = lightId;
    }
}
