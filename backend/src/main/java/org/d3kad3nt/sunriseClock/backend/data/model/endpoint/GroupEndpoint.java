/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
 */

package org.d3kad3nt.sunriseClock.backend.data.model.endpoint;

import androidx.lifecycle.LiveData;
import java.util.List;
import org.d3kad3nt.sunriseClock.backend.data.model.group.RemoteGroup;
import org.d3kad3nt.sunriseClock.backend.data.remote.common.ApiResponse;

public interface GroupEndpoint {

    LiveData<ApiResponse<List<RemoteGroup>>> getGroups();
}
