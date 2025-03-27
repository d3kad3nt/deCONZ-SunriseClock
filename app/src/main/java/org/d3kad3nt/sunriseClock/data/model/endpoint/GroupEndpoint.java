package org.d3kad3nt.sunriseClock.data.model.endpoint;

import androidx.lifecycle.LiveData;
import java.util.List;
import org.d3kad3nt.sunriseClock.data.model.group.RemoteGroup;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiResponse;

public interface GroupEndpoint {

    LiveData<ApiResponse<List<RemoteGroup>>> getGroups();
}
