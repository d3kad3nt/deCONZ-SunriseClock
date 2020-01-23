package org.asdfgamer.sunriseClock.model.light;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = BaseLight.TABLENAME)
public abstract class BaseLight {

    static final String TABLENAME = "light_base";

    @PrimaryKey
    private int id;
    private String friendlyName;

    @ColumnInfo(name = "cap_switchable")
    protected boolean switchable;
    @ColumnInfo(name = "on")
    private boolean on;

    @ColumnInfo(name = "cap_dimmable")
    protected boolean dimmable;
    @ColumnInfo(name = "brightness")
    private int brightness;

    @ColumnInfo(name = "cap_temperaturable")
    protected boolean temperaturable;
    @ColumnInfo(name = "ct")
    private int colorTemperature;

    @ColumnInfo(name = "cap_colorable")
    protected boolean colorable;
    @ColumnInfo(name = "color")
    private int color;

    BaseLight(int id, String friendlyName, boolean switchable, boolean dimmable, boolean temperaturable, boolean colorable) {
        this.id = id;
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

    //TODO: set back to protected after tests
    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }


    protected boolean isOn() {
        return this.on;
    }

    protected void setOn(boolean on) {
        this.on = on;
    }

    protected int getBrightness() {
        return this.brightness;
    }

    protected void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    protected int getColorTemperature() {
        return this.colorTemperature;
    }

    protected void setColorTemperature(int colorTemperature) {
        this.colorTemperature = colorTemperature;
    }
    
    protected int getColor() {
        return this.color;
    }

    protected void setColor(int color) {
        this.color = color;
    }

}
