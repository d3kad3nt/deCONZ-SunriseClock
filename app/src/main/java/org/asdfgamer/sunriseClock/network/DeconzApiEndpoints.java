package org.asdfgamer.sunriseClock.network;

/**
 * Shows all api endpoints for the deconz api.
 * Found at: http://dresden-elektronik.github.io/deconz-rest-doc/rest/
 */
public enum DeconzApiEndpoints {

    CONFIGURATION("config", "Interface to query and modify the gateway configuration."),
    LIGHTS("lights", "Interface for single lights."),
    GROUPS("groups", "Interface for groups of lights."),
    SCENES("scenes", "Interface to the scenes of a group."),
    SCHEDULES("schedules", "Interface for timed commands."),
    TOUCHLINK("touchlink", "Interface for touchlink commands.");

    private final String apiEndpoint;
    private final String description;

    DeconzApiEndpoints(String apiEndpoint, String description) {
        this.apiEndpoint = apiEndpoint;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    @Override
    public String toString() {
        return apiEndpoint + ": " + description;
    }
}
