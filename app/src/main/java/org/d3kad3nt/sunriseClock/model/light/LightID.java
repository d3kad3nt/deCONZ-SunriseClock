package org.d3kad3nt.sunriseClock.model.light;

import java.io.Serializable;

public class LightID implements Serializable {

    static final long serialVersionUID = 427834564L;

    private final long endpointID;

    private final String endpointLightID;

    protected LightID(long endpointID, String endpointLightID){
        this.endpointID = endpointID;
        this.endpointLightID = endpointLightID;
    }

    public long getEndpointID() {
        return endpointID;
    }

    public String getEndpointLightID() {
        return endpointLightID;
    }
}
