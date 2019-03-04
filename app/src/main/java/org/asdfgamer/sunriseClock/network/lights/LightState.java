package org.asdfgamer.sunriseClock.network.lights;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LightState {

    @SerializedName("alert")
    @Expose
    private String alert;
    @SerializedName("bri")
    @Expose
    private Integer bri;
    @SerializedName("colormode")
    @Expose
    private String colormode;
    @SerializedName("ct")
    @Expose
    private Integer ct;
    @SerializedName("effect")
    @Expose
    private String effect;
    @SerializedName("hue")
    @Expose
    private Integer hue;
    @SerializedName("on")
    @Expose
    private Boolean on;
    @SerializedName("reachable")
    @Expose
    private Boolean reachable;
    @SerializedName("sat")
    @Expose
    private Integer sat;
    @SerializedName("xy")
    @Expose
    private List<Double> xy = null;

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public Integer getBri() {
        return bri;
    }

    public void setBri(Integer bri) {
        this.bri = bri;
    }

    public String getColormode() {
        return colormode;
    }

    public void setColormode(String colormode) {
        this.colormode = colormode;
    }

    public Integer getCt() {
        return ct;
    }

    public void setCt(Integer ct) {
        this.ct = ct;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public Integer getHue() {
        return hue;
    }

    public void setHue(Integer hue) {
        this.hue = hue;
    }

    public Boolean getOn() {
        return on;
    }

    public void setOn(Boolean on) {
        this.on = on;
    }

    public Boolean getReachable() {
        return reachable;
    }

    public void setReachable(Boolean reachable) {
        this.reachable = reachable;
    }

    public Integer getSat() {
        return sat;
    }

    public void setSat(Integer sat) {
        this.sat = sat;
    }

    public List<Double> getXy() {
        return xy;
    }

    public void setXy(List<Double> xy) {
        this.xy = xy;
    }

}
