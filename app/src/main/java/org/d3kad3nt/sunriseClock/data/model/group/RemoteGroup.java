package org.d3kad3nt.sunriseClock.data.model.group;

import android.util.Log;

import androidx.annotation.NonNull;

import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointType;
import org.jetbrains.annotations.Contract;

import java.util.List;

public class RemoteGroup {

    private static final String TAG = "RemoteGroup";

    private final EndpointType endpointType;

    private final long endpointId;
    private final String endpointGroupId;

    private final String name;

    private final List<String> endpointLightIds;

    RemoteGroup(EndpointType endpointType,
        long endpointId,
        String endpointGroupId,
        String name,
        List<String> endpointLightIds)
    {
        this.endpointType = endpointType;

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
        this.endpointLightIds = endpointLightIds;
    }

    @NonNull
    @Contract("_ -> new")
    static DbGroup toDbGroup(RemoteGroup remoteGroup) {
        Log.d(TAG, "Converting RemoteGroup to DbGroup...");
        DbGroupBuilder dbGroupBuilder = new DbGroupBuilder();
        //Logic to convert remote group to db group depending on the endpoint type this group originated from.
        DbGroup dbGroup = dbGroupBuilder.setEndpointId(remoteGroup.getEndpointId())
            .setEndpointGroupId(remoteGroup.getEndpointGroupId()).setName(remoteGroup.getName()).build();
        Log.d(TAG, "Converted RemoteGroup with endpointId " +
                   remoteGroup.getEndpointId() +
                   " and endpointGroupId " +
                   remoteGroup.getEndpointGroupId() +
                   " to DbGroup.");
        return dbGroup;
    }

    public EndpointType getEndpointType() {
        return endpointType;
    }

    public long getEndpointId() {
        return endpointId;
    }

    public String getEndpointGroupId() {
        return endpointGroupId;
    }

    public String getName() {
        return name;
    }

    public List<String> getEndpointLightIds() {
        return endpointLightIds;
    }
}
