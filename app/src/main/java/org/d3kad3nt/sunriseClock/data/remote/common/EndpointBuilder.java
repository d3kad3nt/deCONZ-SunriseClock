package org.d3kad3nt.sunriseClock.data.remote.common;

import org.d3kad3nt.sunriseClock.data.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointConfig;

public interface EndpointBuilder {

    EndpointBuilder setConfig(EndpointConfig config);

    BaseEndpoint build();

}
