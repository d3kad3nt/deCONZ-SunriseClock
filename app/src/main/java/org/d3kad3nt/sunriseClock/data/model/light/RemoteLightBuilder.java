package org.d3kad3nt.sunriseClock.data.model.light;

import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointType;

public class RemoteLightBuilder {
    private static final String TAG = "RemoteLightBuilder";

    private EndpointType endpointType;

    private long endpointId;
    private String endpointLightId;

    private String name = "NoName";

    private boolean isSwitchable = false;
    private boolean isOn = false;

    private boolean isDimmable = false;
    private int brightness = 0;

    private boolean isTemperaturable = false;
    private int colorTemperature = 0;

    private boolean isColorable = false;
    private int color = 0;

    public RemoteLightBuilder() {

    }

    public RemoteLightBuilder(RemoteLight light) {
        this.endpointType = light.getEndpointType();
        this.endpointId = light.getEndpointId();
        this.endpointLightId = light.getEndpointLightId();
        this.name = light.getName();
        this.isSwitchable = light.getIsSwitchable();
        this.isOn = light.getIsOn();
        this.isDimmable = light.getIsDimmable();
        this.brightness = light.getBrightness();
        this.isTemperaturable = light.getIsTemperaturable();
        this.colorTemperature = light.getColorTemperature();
        this.isColorable = light.getIsColorable();
        this.color = light.getColor();
    }

    public RemoteLight build() {
        //Todo: Check if endpointId and endpointLightId are set, problem: RemoteLightListTypeAdapter has to set these values after the single light was parsed by GSON
        return new RemoteLight(this.endpointType, this.endpointId, this.endpointLightId, this.name, this.isSwitchable, this.isOn, this.isDimmable, this.brightness, this.isTemperaturable, this.colorTemperature, this.isColorable, this.color);
    }

    public EndpointType getEndpointType() {
        return endpointType;
    }

    public long getEndpointId() {
        return endpointId;
    }

    public String getEndpointLightId() {
        return endpointLightId;
    }

    public String getName() {
        return name;
    }

    public boolean getIsSwitchable() {
        return isSwitchable;
    }

    public boolean getIsOn() {
        return isOn;
    }

    public boolean getIsDimmable() {
        return isDimmable;
    }

    public int getBrightness() {
        return brightness;
    }

    public boolean getIsTemperaturable() {
        return isTemperaturable;
    }

    public int getColorTemperature() {
        return colorTemperature;
    }

    public boolean getIsColorable() {
        return isColorable;
    }

    public int getColor() {
        return color;
    }

    public RemoteLightBuilder setEndpointType(EndpointType endpointType) {
        this.endpointType = endpointType;
        return this;
    }

    public RemoteLightBuilder setEndpointId(long endpointId){
        this.endpointId = endpointId;
        return this;
    }

    public RemoteLightBuilder setEndpointLightId(String endpointLightId){
        this.endpointLightId = endpointLightId;
        return this;
    }

    public RemoteLightBuilder setName(String name){
        this.name = name;
        return this;
    }

    public RemoteLightBuilder setIsSwitchable(boolean isSwitchable){
        this.isSwitchable = isSwitchable;
        return this;
    }

    public RemoteLightBuilder setIsOn(boolean isOn){
        this.isOn = isOn;
        return this;
    }

    public RemoteLightBuilder setIsDimmable(boolean isDimmable){
        this.isDimmable = isDimmable;
        return this;
    }

    public RemoteLightBuilder setBrightness(int brightness){
        this.brightness = brightness;
        return this;
    }

    public RemoteLightBuilder setIsTemperaturable(boolean isTemperaturable){
        this.isTemperaturable = isTemperaturable;
        return this;
    }

    public RemoteLightBuilder setColorTemperature(int colorTemperature){
        this.colorTemperature = colorTemperature;
        return this;
    }

    public RemoteLightBuilder setIsColorable(boolean isColorable){
        this.isColorable = isColorable;
        return this;
    }

    public RemoteLightBuilder setColor(int color){
        this.color = color;
        return this;
    }
}