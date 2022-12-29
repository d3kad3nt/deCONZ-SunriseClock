package org.d3kad3nt.sunriseClock.data.model.endpoint;

import androidx.annotation.NonNull;

import java.util.Date;

public abstract class BaseEndpoint implements LightEndpoint {

    EndpointConfig originalEndpointConfig;

    /**
     * Must be used to initialize the specific endpoint implementation after its (primitive) fields
     * have been populated by eg. Gson. After initialization the endpoint must be ready to perform
     * remote (network) operations.
     */
    public abstract BaseEndpoint init();

    public EndpointConfig getOriginalEndpointConfig() {
        return originalEndpointConfig;
    }

    public void setOriginalEndpointConfig(EndpointConfig originalEndpointConfig) {
        this.originalEndpointConfig = originalEndpointConfig;
    }

    public long getId() {
        return this.originalEndpointConfig.getId();
    }

    public Date getAddedAt(){
        return this.originalEndpointConfig.getAddedAt();
    }

    @NonNull
    @Override
    public String toString() {
        return originalEndpointConfig.toString();
    }
}
