package org.asdfgamer.sunriseClock.model.endpoint.builder;

import org.asdfgamer.sunriseClock.model.endpoint.BaseEndpoint;
import org.asdfgamer.sunriseClock.model.endpoint.EndpointConfig;

public class DeconzEndpointBuilder implements EndpointBuilder {

    EndpointConfig config = null;

    @Override
    public EndpointBuilder setConfig(EndpointConfig config) {
        this.config = config;
        return this;
    }

    @Override
    public BaseEndpoint build() {
        return null;
    }
}
