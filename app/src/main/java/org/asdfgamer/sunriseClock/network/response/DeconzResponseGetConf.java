package org.asdfgamer.sunriseClock.network.response;

public class DeconzResponseGetConf extends DeconzResponseObject {

    /**
     * The api-version of the connected deconz server.
     */
    private String apiVersion;

    /**
     * The ip of the connected deconz server.
     */
    private String ip;

    public DeconzResponseGetConf() {
        super();
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
