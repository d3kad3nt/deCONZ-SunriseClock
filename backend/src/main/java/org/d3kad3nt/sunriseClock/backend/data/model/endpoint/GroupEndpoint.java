package org.d3kad3nt.sunriseClock.backend.data.model.endpoint;

import androidx.lifecycle.LiveData;
import java.util.List;
import org.d3kad3nt.sunriseClock.backend.data.model.group.RemoteGroup;
import org.d3kad3nt.sunriseClock.backend.data.remote.common.ApiResponse;

public interface GroupEndpoint {

    /** @return All lights on this endpoint. */
    LiveData<ApiResponse<List<RemoteGroup>>> getGroups();

    /**
     * @param endpointGroupId String identifying the light on this endpoint.
     * @return The requested light on this endpoint.
     */
    LiveData<ApiResponse<RemoteGroup>> getGroup(String endpointGroupId);
}
