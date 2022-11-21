package org.d3kad3nt.sunriseClock.data.model.endpoint;

import org.d3kad3nt.sunriseClock.data.model.light.ILightUI;

public class UIEndpoint implements ILightUI {
    private final String stringRepresentation;

    public UIEndpoint(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }

    public static UIEndpoint from(BaseEndpoint baseEndpoint){
        return new UIEndpoint(baseEndpoint.toString());
    }

    @Override
    public String getStringRepresentation() {
        return this.stringRepresentation;
    }
}
