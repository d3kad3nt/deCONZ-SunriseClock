package org.d3kad3nt.sunriseClock.data.model.light;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LightID lightID = (LightID) o;
        return endpointID == lightID.endpointID &&
                Objects.equals(endpointLightID, lightID.endpointLightID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(endpointID, endpointLightID);
    }
}
