package org.asdfgamer.sunriseClock.network.response.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Config {
    @SerializedName("UTC")
    @Expose
    private String uTC;
    @SerializedName("apiversion")
    @Expose
    private String apiversion;
    @SerializedName("bridgeid")
    @Expose
    private String bridgeid;
    @SerializedName("datastoreversion")
    @Expose
    private String datastoreversion;
    @SerializedName("devicename")
    @Expose
    private String devicename;
    @SerializedName("dhcp")
    @Expose
    private Boolean dhcp;
    @SerializedName("factorynew")
    @Expose
    private Boolean factorynew;
    @SerializedName("fwversion")
    @Expose
    private String fwversion;
    @SerializedName("gateway")
    @Expose
    private String gateway;
    @SerializedName("ipaddress")
    @Expose
    private String ipaddress;
    @SerializedName("linkbutton")
    @Expose
    private Boolean linkbutton;
    @SerializedName("localtime")
    @Expose
    private String localtime;
    @SerializedName("mac")
    @Expose
    private String mac;
    @SerializedName("modelid")
    @Expose
    private String modelid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("netmask")
    @Expose
    private String netmask;
    @SerializedName("networkopenduration")
    @Expose
    private Integer networkopenduration;
    @SerializedName("panid")
    @Expose
    private Integer panid;
    @SerializedName("portalconnection")
    @Expose
    private String portalconnection;
    @SerializedName("portalservices")
    @Expose
    private Boolean portalservices;
    @SerializedName("proxyaddress")
    @Expose
    private String proxyaddress;
    @SerializedName("proxyport")
    @Expose
    private Integer proxyport;
    @SerializedName("replacesbridgeid")
    @Expose
    private Object replacesbridgeid;
    @SerializedName("rfconnected")
    @Expose
    private Boolean rfconnected;
    @SerializedName("starterkitid")
    @Expose
    private String starterkitid;
    @SerializedName("swversion")
    @Expose
    private String swversion;
    @SerializedName("timeformat")
    @Expose
    private String timeformat;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("websocketnotifyall")
    @Expose
    private Boolean websocketnotifyall;
    @SerializedName("websocketport")
    @Expose
    private Integer websocketport;
    @SerializedName("zigbeechannel")
    @Expose
    private Integer zigbeechannel;

    public String getUTC() {
        return uTC;
    }

    public void setUTC(String uTC) {
        this.uTC = uTC;
    }

    public String getApiversion() {
        return apiversion;
    }

    public void setApiversion(String apiversion) {
        this.apiversion = apiversion;
    }

    public String getBridgeid() {
        return bridgeid;
    }

    public void setBridgeid(String bridgeid) {
        this.bridgeid = bridgeid;
    }

    public String getDatastoreversion() {
        return datastoreversion;
    }

    public void setDatastoreversion(String datastoreversion) {
        this.datastoreversion = datastoreversion;
    }

    public String getDevicename() {
        return devicename;
    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    public Boolean getDhcp() {
        return dhcp;
    }

    public void setDhcp(Boolean dhcp) {
        this.dhcp = dhcp;
    }

    public Boolean getFactorynew() {
        return factorynew;
    }

    public void setFactorynew(Boolean factorynew) {
        this.factorynew = factorynew;
    }

    public String getFwversion() {
        return fwversion;
    }

    public void setFwversion(String fwversion) {
        this.fwversion = fwversion;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public Boolean getLinkbutton() {
        return linkbutton;
    }

    public void setLinkbutton(Boolean linkbutton) {
        this.linkbutton = linkbutton;
    }

    public String getLocaltime() {
        return localtime;
    }

    public void setLocaltime(String localtime) {
        this.localtime = localtime;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getModelid() {
        return modelid;
    }

    public void setModelid(String modelid) {
        this.modelid = modelid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public Integer getNetworkopenduration() {
        return networkopenduration;
    }

    public void setNetworkopenduration(Integer networkopenduration) {
        this.networkopenduration = networkopenduration;
    }

    public Integer getPanid() {
        return panid;
    }

    public void setPanid(Integer panid) {
        this.panid = panid;
    }

    public String getPortalconnection() {
        return portalconnection;
    }

    public void setPortalconnection(String portalconnection) {
        this.portalconnection = portalconnection;
    }

    public Boolean getPortalservices() {
        return portalservices;
    }

    public void setPortalservices(Boolean portalservices) {
        this.portalservices = portalservices;
    }

    public String getProxyaddress() {
        return proxyaddress;
    }

    public void setProxyaddress(String proxyaddress) {
        this.proxyaddress = proxyaddress;
    }

    public Integer getProxyport() {
        return proxyport;
    }

    public void setProxyport(Integer proxyport) {
        this.proxyport = proxyport;
    }

    public Object getReplacesbridgeid() {
        return replacesbridgeid;
    }

    public void setReplacesbridgeid(Object replacesbridgeid) {
        this.replacesbridgeid = replacesbridgeid;
    }

    public Boolean getRfconnected() {
        return rfconnected;
    }

    public void setRfconnected(Boolean rfconnected) {
        this.rfconnected = rfconnected;
    }

    public String getStarterkitid() {
        return starterkitid;
    }

    public void setStarterkitid(String starterkitid) {
        this.starterkitid = starterkitid;
    }

    public String getSwversion() {
        return swversion;
    }

    public void setSwversion(String swversion) {
        this.swversion = swversion;
    }

    public String getTimeformat() {
        return timeformat;
    }

    public void setTimeformat(String timeformat) {
        this.timeformat = timeformat;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getWebsocketnotifyall() {
        return websocketnotifyall;
    }

    public void setWebsocketnotifyall(Boolean websocketnotifyall) {
        this.websocketnotifyall = websocketnotifyall;
    }

    public Integer getWebsocketport() {
        return websocketport;
    }

    public void setWebsocketport(Integer websocketport) {
        this.websocketport = websocketport;
    }

    public Integer getZigbeechannel() {
        return zigbeechannel;
    }

    public void setZigbeechannel(Integer zigbeechannel) {
        this.zigbeechannel = zigbeechannel;
    }

}