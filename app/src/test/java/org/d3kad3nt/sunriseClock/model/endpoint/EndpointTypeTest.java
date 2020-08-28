package org.d3kad3nt.sunriseClock.model.endpoint;

import org.d3kad3nt.sunriseClock.model.endpoint.builder.DeconzEndpointBuilder;

import static org.junit.jupiter.api.Assertions.*;

class EndpointTypeTest {

    @org.junit.jupiter.api.Test
    void deconzGetID() {
        assertEquals(0, EndpointType.DECONZ.getId());
    }

    @org.junit.jupiter.api.Test
    void deconzGetBuilder() {
        assertTrue(EndpointType.DECONZ.getBuilder() instanceof DeconzEndpointBuilder);
    }
}