package org.d3kad3nt.sunriseClock.data.model;

import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointType;
import org.d3kad3nt.sunriseclock.util.LogUtil;

public abstract class RemoteEndpointEntity {

    private final EndpointType endpointType;

    private final long endpointId;

    private final String endpointEntityId;

    private final String name;

    public RemoteEndpointEntity(EndpointType endpointType, long endpointId, String endpointEntityId, String name) {
        this.endpointType = endpointType;

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

    /** @return Type of the remote endpoint. */
    public EndpointType getEndpointType() {
        return endpointType;
    }

    /**
     * @return Foreign key of the remote endpoint that this entity belongs to. Only one endpoint object id (specific for
     *     that endpoint!) can exist for a single endpoint.
     */
    public long getEndpointId() {
        return endpointId;
    }

    /** @return Identifier for this entity as used by the remote endpoint. */
    public String getEndpointEntityId() {
        return endpointEntityId;
    }

    /** @return Name that can be used by the user to identify this object. */
    public String getName() {
        return name;
    }
}
