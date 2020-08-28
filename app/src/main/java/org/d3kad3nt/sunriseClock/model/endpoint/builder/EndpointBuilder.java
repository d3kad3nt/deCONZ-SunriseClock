package org.d3kad3nt.sunriseClock.model.endpoint.builder;

import org.d3kad3nt.sunriseClock.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.model.endpoint.EndpointConfig;

public interface EndpointBuilder {

    EndpointBuilder setConfig(EndpointConfig config);

    BaseEndpoint build();

}
