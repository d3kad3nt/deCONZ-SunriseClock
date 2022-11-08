package org.d3kad3nt.sunriseClock.data.model.light;

import org.d3kad3nt.sunriseClock.data.model.endpoint.BaseEndpoint;

public class LightUI implements ILightUI {
    private final String stringRepresentation;

    public LightUI(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }

    public static LightUI from(BaseEndpoint baseEndpoint){
        return new LightUI(baseEndpoint.toString());
    }

    @Override
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }
}
