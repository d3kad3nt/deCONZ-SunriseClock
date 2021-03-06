package org.asdfgamer.sunriseClock.network.lights.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Light {

    private transient int lightId;

    @SerializedName("etag")
    @Expose
    private String etag;
    @SerializedName("hascolor")
    @Expose
    private Boolean hascolor;
    @SerializedName("manufacturername")
    @Expose
    private String manufacturername;
    @SerializedName("modelid")
    @Expose
    private String modelid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("state")
    @Expose
    private LightState state;
    @SerializedName("swversion")
    @Expose
    private String swversion;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("uniqueid")
    @Expose
    private String uniqueid;

    public int getLightId() {
        return lightId;
    }

    public void setLightId(int lightId) {
        this.lightId = lightId;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public Boolean getHascolor() {
        return hascolor;
    }

    public void setHascolor(Boolean hascolor) {
        this.hascolor = hascolor;
    }

    public String getManufacturername() {
        return manufacturername;
    }

    public void setManufacturername(String manufacturer) {
        this.manufacturername = manufacturer;
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

    public LightState getState() {
        return state;
    }

    public void setState(LightState state) {
        this.state = state;
    }

    public String getSwversion() {
        return swversion;
    }

    public void setSwversion(String swversion) {
        this.swversion = swversion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

}
