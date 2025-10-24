package org.d3kad3nt.sunriseClock.backend.data.model.endpoint;

import androidx.lifecycle.LiveData;
import java.util.List;
import okhttp3.ResponseBody;
import org.d3kad3nt.sunriseClock.backend.data.model.group.RemoteGroup;
import org.d3kad3nt.sunriseClock.backend.data.remote.common.ApiResponse;

public interface GroupEndpoint {

    LiveData<ApiResponse<List<RemoteGroup>>> getGroups();

    LiveData<ApiResponse<RemoteGroup>> getGroup(String endpointGroupId);

    LiveData<ApiResponse<ResponseBody>> setGroupOnState(String endpointGroupId, boolean newState);
}
