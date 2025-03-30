/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
 */

package org.d3kad3nt.sunriseClock.backend.data.model.endpoint;

public interface IEndpointUI {

    String getName();

    String getStringRepresentation();

    long getId();

    EndpointType getType();
}
