package org.d3kad3nt.sunriseClock.data.model.endpoint;

import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.data.model.group.RemoteGroup;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiResponse;

import java.util.List;

public interface GroupEndpoint {

    LiveData<ApiResponse<List<RemoteGroup>>> getGroups();
}
