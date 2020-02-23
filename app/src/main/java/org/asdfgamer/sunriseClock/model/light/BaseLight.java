package org.asdfgamer.sunriseClock.model.light;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.asdfgamer.sunriseClock.model.LightEndpoint;
import org.asdfgamer.sunriseClock.model.endpoint.EndpointConfig;

@Entity(tableName = BaseLight.TABLENAME,
        indices = {@Index("endpointUUID")},
        foreignKeys = @ForeignKey(entity = EndpointConfig.class,
                parentColumns = "endpointID",
                childColumns = "endpointUUID",
                onDelete = ForeignKey.CASCADE))
public class BaseLight implements Light {

    static final String TABLENAME = "light";

    @PrimaryKey
    @ColumnInfo(name = "lightID")
    private int id;
    @ColumnInfo(name = "endpointUUID")
    private int endpointUUID;

    @ColumnInfo(name = "friendlyName")
    private String friendlyName;

    @ColumnInfo(name = "cap_switchable")
    protected boolean switchable;
    @ColumnInfo(name = "on")
    protected boolean on;

    @ColumnInfo(name = "cap_dimmable")
    protected boolean dimmable;
    @ColumnInfo(name = "brightness")
    protected int brightness;

    @ColumnInfo(name = "cap_temperaturable")
    protected boolean temperaturable;
    @ColumnInfo(name = "colorTemperature")
    protected int colorTemperature;

    @ColumnInfo(name = "cap_colorable")
    protected boolean colorable;
    @ColumnInfo(name = "color")
    protected int color;

    @Ignore
    private transient LightEndpoint endpoint;

    public BaseLight(int id, int endpointUUID, String friendlyName, boolean switchable, boolean dimmable, boolean temperaturable, boolean colorable) {
        this.id = id;
        this.endpointUUID = endpointUUID;

        this.friendlyName = friendlyName;

        this.switchable = switchable;
        this.dimmable = dimmable;
        this.temperaturable = temperaturable;
        this.colorable = colorable;
    }

    public int getId() {
        return this.id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public String getFriendlyName() {
        return this.friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public void observeState(LightEndpoint endpoint){
        this.endpoint = endpoint;
        Log.e("BaseLight", "observeState: set endpoint" );
    }


    public LightEndpoint getEndpoint(){
        return this.endpoint;
    }

    public boolean isOn() {
        return this.on;
    }

    public void requestSetOn(boolean on) {
        endpoint.requestSetOn(this,on);
    }

    public void setOn(boolean on){
        this.on = on;
    }


    public int getBrightness() {
        return this.brightness;
    }

    public void requestSetBrightness(int brightness) {
        endpoint.requestSetBrightness(this,brightness);
    }

    public void setBrightness(int brightness){
        this.brightness = brightness;
    }

    public int getColorTemperature() {
        return this.colorTemperature;
    }

    public void requestSetColorTemperature(int colorTemperature) {
        //TODO: Only useful for testing observable Lights (as long as the endpoint for a light is null and e.g. requestSetColor is not usable)
        //endpoint.requestSetColorTemperature(this,colorTemperature);
        this.colorTemperature = colorTemperature;
    }

    public void setColorTemperature(int colorTemperature) {
        this.colorTemperature = colorTemperature;
    }

    public int getColor() {
        return this.color;
    }

    @Override
    public void requestSetColor(int color) {
        endpoint.requestSetColor(this, color);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getEndpointUUID() {
        return endpointUUID;
    }

}
