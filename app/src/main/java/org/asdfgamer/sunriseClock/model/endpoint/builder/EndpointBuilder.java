package org.asdfgamer.sunriseClock.model.endpoint.builder;

import org.asdfgamer.sunriseClock.model.endpoint.BaseEndpoint;
import org.asdfgamer.sunriseClock.model.endpoint.EndpointConfig;

public interface EndpointBuilder {

    EndpointBuilder setConfig(EndpointConfig config);

    BaseEndpoint build();

}
