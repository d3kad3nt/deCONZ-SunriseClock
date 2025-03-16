package org.d3kad3nt.sunriseClock.model.endpoint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointType;
import org.d3kad3nt.sunriseClock.data.remote.deconz.DeconzEndpointBuilder;

class EndpointTypeTest {

    @org.junit.jupiter.api.Test
    void deconzGetID() {
        assertEquals(0, EndpointType.DECONZ.getId());
    }

    @org.junit.jupiter.api.Test
    void deconzGetBuilder() {
        assertInstanceOf(DeconzEndpointBuilder.class, EndpointType.DECONZ.getBuilder());
    }
}
