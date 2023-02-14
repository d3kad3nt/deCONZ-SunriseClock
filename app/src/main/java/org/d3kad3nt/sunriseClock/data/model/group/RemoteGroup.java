package org.d3kad3nt.sunriseClock.data.model.group;

import android.util.Log;

import androidx.annotation.NonNull;

import org.d3kad3nt.sunriseClock.data.model.RemoteEndpointEntity;
import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointType;
import org.jetbrains.annotations.Contract;

import java.util.List;

public class RemoteGroup extends RemoteEndpointEntity {

    private static final String TAG = "RemoteGroup";

    private final List<String> endpointLightIds;

    RemoteGroup(EndpointType endpointType, long endpointId, String endpointGroupId, String name,
                List<String> endpointLightIds) {
        super(endpointType, endpointId, endpointGroupId, name);

        this.endpointLightIds = endpointLightIds;
    }

    @NonNull
    @Contract("_ -> new")
    static DbGroup toDbGroup(RemoteGroup remoteGroup) {
        Log.d(TAG, "Converting RemoteGroup to DbGroup...");
        DbGroupBuilder dbGroupBuilder = new DbGroupBuilder();
        // Logic to convert remote group to db group.
        // The endpoint type could be used to implement conversions depending on the type of the remote endpoint
        // this entity originated from.
        DbGroup dbGroup = dbGroupBuilder.setEndpointId(remoteGroup.getEndpointId())
            .setEndpointGroupId(remoteGroup.getEndpointEntityId()).setName(remoteGroup.getName()).build();
        Log.d(TAG, "Converted RemoteGroup with endpointId " + remoteGroup.getEndpointId() + " and endpointGroupId " +
            remoteGroup.getEndpointEntityId() + " to DbGroup.");
        return dbGroup;
    }

    public List<String> getEndpointLightIds() {
        return endpointLightIds;
    }
}
