package org.asdfgamer.sunriseClock.model.endpoint.builder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.asdfgamer.sunriseClock.model.endpoint.BaseEndpoint;
import org.asdfgamer.sunriseClock.model.endpoint.EndpointConfig;
import org.asdfgamer.sunriseClock.model.endpoint.deconz.DeconzEndpoint;

public class DeconzEndpointBuilder implements EndpointBuilder {

    private EndpointConfig config = null;

    @Override
    public EndpointBuilder setConfig(EndpointConfig config) {
        this.config = config;
        return this;
    }

    @Override
    public BaseEndpoint build() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(config.getJsonConfig(), DeconzEndpoint.class);
    }
}
