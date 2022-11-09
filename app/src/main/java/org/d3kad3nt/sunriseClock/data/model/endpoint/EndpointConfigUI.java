package org.d3kad3nt.sunriseClock.data.model.endpoint;

import org.d3kad3nt.sunriseClock.data.model.light.ILightUI;

public class EndpointConfigUI implements ILightUI {
    private final String stringRepresentation;

    public EndpointConfigUI(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }

    public static EndpointConfigUI from(BaseEndpoint baseEndpoint){
        return new EndpointConfigUI(baseEndpoint.toString());
    }

    @Override
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }
}
