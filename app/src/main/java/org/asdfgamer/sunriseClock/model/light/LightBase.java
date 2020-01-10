package org.asdfgamer.sunriseClock.model.light;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = LightBase.TABLENAME)
public abstract class LightBase {

    static final String TABLENAME = "light_base";

    @PrimaryKey
    private int id;
    private String friendlyName;

    public static boolean SWITCHABLE = false;
    @ColumnInfo(name = "on")
    private boolean on;

    public static boolean DIMMABLE = false;
    @ColumnInfo(name = "brightness")
    private int brightness;

    public static boolean TEMPERATURABLE = false;
    @ColumnInfo(name = "ct")
    private int colorTemperature;

    public static boolean COLORABLE = false;
    @ColumnInfo(name = "color")
    private int color;

    LightBase(int id, String friendlyName) {
        this.id = id;
        this.friendlyName = friendlyName;
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

    protected void setFriendlyName(String friendlyName) {
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
