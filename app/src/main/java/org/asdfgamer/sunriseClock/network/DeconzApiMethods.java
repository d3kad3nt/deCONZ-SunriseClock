package org.asdfgamer.sunriseClock.network;

import com.android.volley.Request;

/**
 * Shows all api endpoints for the deconz api.
 * Found at: http://dresden-elektronik.github.io/deconz-rest-doc/rest/
 */
public enum DeconzApiMethods {

    GET(Request.Method.GET, "GET", "Query the content of a ressource."),
    PUT(Request.Method.PUT, "PUT", "Modifies a existing ressource."),
    POST(Request.Method.POST, "POST", "Creates a new ressource which did not exist before."),
    DELETE(Request.Method.DELETE, "DELETE", "Deletes a ressource.");

    /* Internal representation of the method in volley. */
    private final int volleyMethod;

    /* Method name in human-readable format (eg. 'GET'). */
    private final String method;

    /* Method description. */
    private final String description;

    DeconzApiMethods(int volleyMethod, String method, String description) {
        this.volleyMethod = volleyMethod;
        this.method = method;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getVolleyMethod() {
        return volleyMethod;
    }

    public String getMethod() {
        return method;
    }
}
