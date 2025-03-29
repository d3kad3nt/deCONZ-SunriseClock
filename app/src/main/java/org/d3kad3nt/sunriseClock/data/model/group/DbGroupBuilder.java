package org.d3kad3nt.sunriseClock.data.model.group;

public class DbGroupBuilder {

    private long endpointId;
    private String endpointGroupId;

    private String name = "NoName";

    /** Builder for constructing DbGroups. */
    public DbGroupBuilder() {}

    public DbGroup build() {
        // Validator logic is defined inside the constructor of DbGroup (separation of concern).
        return new DbGroup(endpointId, endpointGroupId, name);
    }

    /**
     * @param endpointId Foreign key (Room/SQLite) of the remote endpoint that this group belongs to. Only one endpoint
     *     group id (specific for that endpoint!) can exist for a single endpoint.
     */
    public DbGroupBuilder setEndpointId(long endpointId) {
        this.endpointId = endpointId;
        return this;
    }

    /**
     * This field enables the remote endpoint to identify the correct group. A remote endpoint cannot work with the
     * groupId.
     *
     * @param endpointGroupId Identifier for this group inside (!) the remote endpoint.
     */
    public DbGroupBuilder setEndpointGroupId(String endpointGroupId) {
        this.endpointGroupId = endpointGroupId;
        return this;
    }

    /** @param name Name that can be used by the user to identify this group. */
    public DbGroupBuilder setName(String name) {
        this.name = name;
        return this;
    }
}
