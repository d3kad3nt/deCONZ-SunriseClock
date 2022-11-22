package org.d3kad3nt.sunriseClock.data.model.endpoint;

import androidx.annotation.NonNull;

import org.d3kad3nt.sunriseClock.data.model.light.ILightUI;
import org.jetbrains.annotations.Contract;

public class UIEndpoint implements ILightUI {
    private final String stringRepresentation;
    private final long id;

    UIEndpoint(@NonNull String stringRepresentation, long id) {
        this.stringRepresentation = stringRepresentation;
        this.id = id;
    }

    @NonNull
    @Contract("_ -> new")
    public static UIEndpoint from(EndpointConfig baseEndpoint){
        return new UIEndpoint(baseEndpoint.toString(), baseEndpoint.getId());
    }

    @Override
    public String getStringRepresentation() {
        return stringRepresentation;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }
}
