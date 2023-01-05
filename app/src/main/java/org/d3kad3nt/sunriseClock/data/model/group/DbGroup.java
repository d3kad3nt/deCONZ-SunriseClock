package org.d3kad3nt.sunriseClock.data.model.group;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointConfig;
import org.jetbrains.annotations.Contract;

@Entity(tableName = DbGroup.TABLENAME,
    indices = {@Index(value = {"endpoint_id", "endpoint_group_id"},
        unique = true)},
    // A DbGroup is always bound to a single endpoint. It cannot exist without one:
    // Therefore Room is instructed to delete this DbGroup if the endpoint gets deleted.
    foreignKeys = @ForeignKey(entity = EndpointConfig.class,
        parentColumns = "endpointId",
        childColumns = "endpoint_id",
        onDelete = ForeignKey.CASCADE))
public class DbGroup {

    @Ignore
    public static final String TABLENAME = "group";

    @Ignore
    private static final String TAG = "DbGroup";

    @ColumnInfo(name = "endpoint_id")
    private final long endpointId;
    @ColumnInfo(name = "endpoint_group_id")
    @NonNull // Set SQLITE notNull attribute, for primitive types this is set automatically (but this is a string).
    private final String endpointGroupId;

    @ColumnInfo(name = "name")
    @NonNull // Set SQLITE notNull attribute, for primitive types this is set automatically (but this is a string).
    private final String name;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "group_id")
    private long groupId; // Cannot be final because Room must be able to set the groupId after it was auto-generated.

    /**
     * Create a new object that represents a group in the app's Room database. This constructor has to be public for
     * Room to be able to create an object. This should not be otherwise accessed!
     */
    public DbGroup(long endpointId, String endpointGroupId, String name) {
        if (endpointId != 0L) {
            this.endpointId = endpointId;
        } else {
            Log.e(TAG, "The given endpointId cannot be 0!");
            throw new IllegalArgumentException("The given endpointId cannot be 0!");
        }

        if (endpointGroupId != null && !endpointGroupId.isEmpty()) {
            this.endpointGroupId = endpointGroupId;
        } else {
            Log.e(TAG, "The given endpointGroupId string cannot be null or empty!");
            throw new IllegalArgumentException("The given endpointGroupId string cannot be null or empty!");
        }

        this.name = name;
    }

    @NonNull
    @Contract("_ -> new")
    public static DbGroup from(@NonNull RemoteGroup remoteGroup) {
        return RemoteGroup.toDbGroup(remoteGroup);
    }

    /**
     * @return Identifier for this group (inside the database).
     */
    public long getGroupId() {
        return groupId;
    }

    /**
     * This setter has to be public for Room to be able to set the auto-generated id. It must not be used outside of
     * Room!
     *
     * @param groupId The auto-generated identifier of this group, not depending on the (endpoint-specific)
     *                endpointGroupId.
     */
    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    /**
     * @return Foreign key (Room/SQLite) of the remote endpoint that this group belongs to. Only one endpoint group id
     * (specific for that endpoint!) can exist for a single endpoint.
     */
    public long getEndpointId() {
        return endpointId;
    }

    /**
     * This field enables the remote endpoint to identify the correct group. A remote endpoint cannot work with the
     * groupId.
     *
     * @return Identifier for this group inside (!) the remote endpoint.
     */
    @NonNull
    public String getEndpointGroupId() {
        return endpointGroupId;
    }

    /**
     * @return Name that can be used by the user to identify this group.
     */
    @NonNull
    public String getName() {
        return name;
    }
}
