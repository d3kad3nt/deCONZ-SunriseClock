package org.asdfgamer.sunriseClock.model.light;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.asdfgamer.sunriseClock.model.BaseLightObserver;

@Entity(tableName = BaseLight.TABLENAME)
public class BaseLight implements Light {

    static final String TABLENAME = "light_base";

    @PrimaryKey
    private int id;
    @ColumnInfo(name = "friendlyName")
    private String friendlyName;

    @ColumnInfo(name = "enpointUUID")
    private int endpointUUID;

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
    private BaseLightObserver observer;

    public BaseLight(int id, String friendlyName, boolean switchable, boolean dimmable, boolean temperaturable, boolean colorable, int endpointUUID) {
        this.id = id;
        this.friendlyName = friendlyName;
        this.endpointUUID = endpointUUID;

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

    //TODO: set back to protected after tests
    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public void observeState(BaseLightObserver endpoint){
        this.observer = endpoint;
        Log.e("BaseLight", "observeState: set observer" );
    }


    public BaseLightObserver getObserver(){
        return observer;
    }

    public boolean isOn() {
        return this.on;
    }

    public void requestSetOn(boolean on) {
        observer.setOn(this,on);
    }

    public void setOn(boolean on){
        this.on = on;
    }


    public int getBrightness() {
        return this.brightness;
    }

    public void requestSetBrightness(int brightness) {
        observer.requestSetBrightness(this,brightness);
    }

    public void setBrightness(int brightness){
        this.brightness = brightness;
    }

    public int getColorTemperature() {
        return this.colorTemperature;
    }

    public void requestSetColorTemperature(int colorTemperature) {
        observer.requestSetColorTemperature(this,colorTemperature);
    }

    public void setColorTemperature(int colorTemperature) {
        this.colorTemperature = colorTemperature;
    }

    public int getColor() {
        return this.color;
    }

    public void requestSetColor(int color) {
        observer.requestSetColor(this,color);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getEndpointUUID() {
        return endpointUUID;
    }

}
