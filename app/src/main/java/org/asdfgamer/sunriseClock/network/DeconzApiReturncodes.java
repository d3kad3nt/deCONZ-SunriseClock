package org.asdfgamer.sunriseClock.network;

/**
 * Shows error codes for the deconz api.
 * Found at: https://dresden-elektronik.github.io/deconz-rest-doc/errors/
 */
public enum DeconzApiReturncodes {

    //TODO: Use localized Strings
    OK(200, "Request succeded"),
    Created(201, "A new ressource was created"),
    Accepted(202, "Request will be processed but isn't finished yet"),
    Not_Modified(304, "Is returned if the request had the If-None-Match header and the ETag on the resource was the same."),
    Bad_Request(400, "The request was not formatted as expected or missing parameters"),
    Unauthorized(401, "Authorization failed"),
    Forbidden(403, "The caller has no rights to access the requested URI"),
    Resource_Not_Found(404, "The requested resource (light, group, ...) was not found"),
    Service_Unavailable(503, "The device is not connected to the network or too busy to handle further requests");


    private final int code;
    private final String description;

    DeconzApiReturncodes(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
