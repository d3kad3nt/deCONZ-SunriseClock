package org.d3kad3nt.sunriseClock.data.model;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import java.util.Objects;
import org.d3kad3nt.sunriseclock.util.LogUtil;

public abstract class DbEndpointEntity {

    @ColumnInfo(name = "endpoint_id")
    private final long endpointId;

    @ColumnInfo(name = "id_on_endpoint")
    @NonNull // Set SQLITE notNull attribute, for primitive types this is set automatically (but
    // this is a string).
    private final String endpointEntityId;

    @ColumnInfo(name = "name", defaultValue = "No Name")
    @NonNull // Set SQLITE notNull attribute, for primitive types this is set automatically (but
    // this is a string).
    private final String name;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id; // Cannot be final because Room must be able to set the groupId after it was
    // auto-generated.

    public DbEndpointEntity(long endpointId, String endpointEntityId, String name) {

        if (endpointId != 0L) {
            this.endpointId = endpointId;
        } else {
            LogUtil.e("The given endpointId cannot be 0!");
            throw new IllegalArgumentException("The given endpointId cannot be 0!");
        }

        if (endpointEntityId != null && !endpointEntityId.isEmpty()) {
            this.endpointEntityId = endpointEntityId;
        } else {
            LogUtil.e("The given endpointEntityId string cannot be null or empty!");
            throw new IllegalArgumentException("The given endpointEntityId string cannot be null or empty!");
        }

        this.name = name;
    }

    /**
     * @return Foreign key of the remote endpoint that this entity belongs to. Only one endpoint object id (specific for
     *     that endpoint!) can exist for a single endpoint.
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

    /** @return Name that can be used by the user to identify this object. */
    @NonNull
    public String getName() {
        return name;
    }

    /** @return Auto-generated identifier for this object (inside the database). */
    public long getId() {
        return id;
    }

    /**
     * This setter has to be public for Room to be able to set the auto-generated id. It must not be used outside of
     * Room!
     *
     * @param id The auto-generated identifier of this group, not depending on the (endpoint-specific) endpointGroupId.
     */
    public void setId(long id) {
        this.id = id;
    }

    /** @return Name of the table containing these entities in the database. */
    public abstract String getTABLENAME();

    @Override
    @CallSuper
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final DbEndpointEntity that)) {
            return false;
        }
        return endpointId == that.endpointId
                && id == that.id
                && Objects.equals(endpointEntityId, that.endpointEntityId)
                && Objects.equals(name, that.name);
    }

    @Override
    @CallSuper
    public int hashCode() {
        return Objects.hash(endpointId, endpointEntityId, name, id);
    }
}
