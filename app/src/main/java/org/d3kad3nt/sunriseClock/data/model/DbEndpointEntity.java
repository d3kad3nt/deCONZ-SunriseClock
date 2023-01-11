package org.d3kad3nt.sunriseClock.data.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public abstract class DbEndpointEntity {

    private static final String TAG = "DbEndpointEntity";

    @ColumnInfo(name = "endpoint_id")
    private final long endpointId;

    @ColumnInfo(name = "id_on_endpoint")
    @NonNull // Set SQLITE notNull attribute, for primitive types this is set automatically (but this is a string).
    private final String endpointEntityId;

    @ColumnInfo(name = "name")
    @NonNull // Set SQLITE notNull attribute, for primitive types this is set automatically (but this is a string).
    private final String name;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id; // Cannot be final because Room must be able to set the groupId after it was auto-generated.

    public DbEndpointEntity(long endpointId, String endpointEntityId, String name) {

        if (endpointId != 0L) {
            this.endpointId = endpointId;
        } else {
            Log.e(TAG, "The given endpointId cannot be 0!");
            throw new IllegalArgumentException("The given endpointId cannot be 0!");
        }

        if (endpointEntityId != null && !endpointEntityId.isEmpty()) {
            this.endpointEntityId = endpointEntityId;
        } else {
            Log.e(TAG, "The given endpointObjectId string cannot be null or empty!");
            throw new IllegalArgumentException("The given endpointObjectId string cannot be null or empty!");
        }

        this.name = name;
    }

    /**
     * @return Foreign key of the remote endpoint that this object belongs to. Only one endpoint object id (specific
     * for that endpoint!) can exist for a single endpoint.
     */
    public long getEndpointId() {
        return endpointId;
    }

    /**
     * This field enables the remote endpoint to identify the correct entity. A remote endpoint cannot work with the
     * (auto-generated) id that we use internally.
     *
     * @return Identifier for this entity as used by the remote endpoint.
     */
    @NonNull
    public String getEndpointEntityId() {
        return endpointEntityId;
    }

    /**
     * @return Name that can be used by the user to identify this object.
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * @return Auto-generated identifier for this object (inside the database).
     */
    public long getId() {
        return id;
    }

    /**
     * This setter has to be public for Room to be able to set the auto-generated id. It must not be used outside of
     * Room!
     *
     * @param id The auto-generated identifier of this group, not depending on the (endpoint-specific)
     *           endpointGroupId.
     */
    public void setId(long id) {
        this.id = id;
    }
}
