package org.d3kad3nt.sunriseClock.backend.data.model.group;

import java.util.ArrayList;
import java.util.List;
import org.d3kad3nt.sunriseClock.backend.data.model.endpoint.EndpointType;

public class RemoteGroupBuilder {

    private EndpointType endpointType;

    private long endpointId;
    private String endpointGroupId;

    private String name = "NoName";

    private List<String> endpointLightIds = new ArrayList<>();

    /** Builder for constructing RemoteGroups. */
    public RemoteGroupBuilder() {}

    public RemoteGroup build() {
        if (endpointType == null) {
            throw new IllegalStateException(
                    "RemoteLightBuilder cannot build this light without an endpoint type! Check remote light parsing "
                            + "logic.");
        }
        return new RemoteGroup(endpointType, endpointId, endpointGroupId, name, endpointLightIds);
    }

    public RemoteGroupBuilder setEndpointType(EndpointType endpointType) {
        this.endpointType = endpointType;
        return this;
    }

    public RemoteGroupBuilder setEndpointId(long endpointId) {
        this.endpointId = endpointId;
        return this;
    }

    public RemoteGroupBuilder setEndpointGroupId(String endpointGroupId) {
        this.endpointGroupId = endpointGroupId;
        return this;
    }

    public RemoteGroupBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public RemoteGroupBuilder setEndpointLightIds(List<String> endpointLightIds) {
        this.endpointLightIds = endpointLightIds;
        return this;
    }
}
