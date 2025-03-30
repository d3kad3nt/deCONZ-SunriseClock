package org.d3kad3nt.sunriseClock.data.model.group;

import androidx.annotation.NonNull;
import java.util.List;
import org.d3kad3nt.sunriseClock.data.model.RemoteEndpointEntity;
import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointType;
import org.d3kad3nt.sunriseclock.util.LogUtil;
import org.jetbrains.annotations.Contract;

public class RemoteGroup extends RemoteEndpointEntity {

    private final List<String> endpointLightIds;

    RemoteGroup(
            EndpointType endpointType,
            long endpointId,
            String endpointGroupId,
            String name,
            List<String> endpointLightIds) {
        super(endpointType, endpointId, endpointGroupId, name);

        this.endpointLightIds = endpointLightIds;
    }

    @NonNull
    @Contract("_ -> new")
    static DbGroup toDbGroup(RemoteGroup remoteGroup) {
        LogUtil.d("Converting RemoteGroup to DbGroup...");
        DbGroupBuilder dbGroupBuilder = new DbGroupBuilder();
        // Logic to convert remote group to db group.
        // The endpoint type could be used to implement conversions depending on the type of the
        // remote endpoint
        // this entity originated from.
        DbGroup dbGroup = dbGroupBuilder
                .setEndpointId(remoteGroup.getEndpointId())
                .setEndpointGroupId(remoteGroup.getEndpointEntityId())
                .setName(remoteGroup.getName())
                .build();
        LogUtil.d(
                "Converted RemoteGroup with endpointId %d and endpointGroupId %s to DbGroup.",
                remoteGroup.getEndpointId(), remoteGroup.getEndpointEntityId());
        return dbGroup;
    }

    public List<String> getEndpointLightIds() {
        return endpointLightIds;
    }
}
