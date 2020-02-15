package org.asdfgamer.sunriseClock.model.light;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.asdfgamer.sunriseClock.model.endpoint.BaseMasterEndpoint;

@Entity(tableName = BaseLight.TABLENAME)

public class BaseLight {

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

    BaseLight(int id, String friendlyName, boolean switchable, boolean dimmable, boolean temperaturable, boolean colorable, int endpointUUID) {
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


    boolean isOn() {
        return this.on;
    }

    void requestSetOn(boolean on) {
//        this.endpoint.baseLightEndpoint.setOn(this, on);
    }

    int getBrightness() {
        return this.brightness;
    }

    void requestSetBrightness(int brightness) {
//        this.endpoint.baseLightEndpoint.setBrightness(this, brightness);
    }

    int getColorTemperature() {
        return this.colorTemperature;
    }

    void requestSetColorTemperature(int colorTemperature) {
//        this.endpoint.baseLightEndpoint.setColorTemperature(this, colorTemperature);
    }

    int getColor() {
        return this.color;
    }

    void requestSetColor(int color) {
//        this.endpoint.baseLightEndpoint.setColor(this, color);
    }

    public int getEndpointUUID() {
        return endpointUUID;
    }

}
