package org.d3kad3nt.sunriseClock.data.model;

import android.util.Log;

import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointType;

public abstract class RemoteEndpointEntity {

    private static final String TAG = "RemoteEndpointEntity";

    private final EndpointType endpointType;

    private final long endpointId;

    private final String endpointEntityId;

    private final String name;

    public RemoteEndpointEntity(EndpointType endpointType, long endpointId, String endpointEntityId, String name) {
        this.endpointType = endpointType;

        if (endpointId != 0L) {
            this.endpointId = endpointId;
        } else {
            Log.e(TAG, "The given endpointId cannot be 0!");
            throw new IllegalArgumentException("The given endpointId cannot be 0!");
        }

        if (endpointEntityId != null && !endpointEntityId.isEmpty()) {
            this.endpointEntityId = endpointEntityId;
        } else {
            Log.e(TAG, "The given endpointEntityId string cannot be null or empty!");
            throw new IllegalArgumentException("The given endpointEntityId string cannot be null or empty!");
        }

        this.name = name;
    }

    /**
     * @return Type of the remote endpoint.
     */
    public EndpointType getEndpointType() {
        return endpointType;
    }

    /**
     * @return Foreign key of the remote endpoint that this entity belongs to. Only one endpoint object id (specific
     * for that endpoint!) can exist for a single endpoint.
     */
    public long getEndpointId() {
        return endpointId;
    }

    /**
     * @return Identifier for this entity as used by the remote endpoint.
     */
    public String getEndpointEntityId() {
        return endpointEntityId;
    }

    /**
     * @return Name that can be used by the user to identify this object.
     */
    public String getName() {
        return name;
    }
}
