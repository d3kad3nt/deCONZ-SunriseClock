/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
 */

package org.d3kad3nt.sunriseClock.backend.data.remote.common;

import org.d3kad3nt.sunriseClock.backend.data.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.backend.data.model.endpoint.EndpointConfig;

public interface EndpointBuilder {

    EndpointBuilder setConfig(EndpointConfig config);

    BaseEndpoint build();
}
