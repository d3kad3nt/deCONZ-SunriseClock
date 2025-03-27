package org.d3kad3nt.sunriseClock.data.model;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import java.util.Locale;
import java.util.Objects;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public abstract class UIEndpointEntity<T extends UIEndpointEntity<T>> implements ListItem, Comparable<T> {

    private final long id;
    private final long endpointId;

    private final String name;

    public UIEndpointEntity(long id, long endpointId, String name) {

        if (id != 0L) {
            this.id = id;
        } else {
            LogUtil.e("The given id cannot be 0!");
            throw new IllegalArgumentException("The given id cannot be 0!");
        }

        if (endpointId != 0L) {
            this.endpointId = endpointId;
        } else {
            LogUtil.e("The given endpointId cannot be 0!");
            throw new IllegalArgumentException("The given endpointId cannot be 0!");
        }

        this.name = name;
    }

    /** @return Auto-generated identifier for this object (inside the database). */
    public long getId() {
        return id;
    }

    /**
     * @return Foreign key of the remote endpoint that this entity belongs to. Only one endpoint object id (specific for
     *     that endpoint!) can exist for a single endpoint.
     */
    public long getEndpointId() {
        return endpointId;
    }

    /** @return Name that can be used by the user to identify this object. */
    @NonNull
    public String getName() {
        return name;
    }

    @Override
    public int compareTo(final T o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    @CallSuper
    public int hashCode() {
        return Objects.hash(id, endpointId, name);
    }

    @Override
    @CallSuper
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final UIEndpointEntity that)) {
            return false;
        }
        return id == that.id && endpointId == that.endpointId && Objects.equals(name, that.name);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(
                Locale.ENGLISH,
                "UiEndpointEntity (ListItem type %s) with id %d, endpointId %d and " + "name '%s'.",
                getType(),
                getId(),
                getEndpointId(),
                getName());
    }
}
