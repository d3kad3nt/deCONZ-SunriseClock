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
        indices = {@Index("endpointId")},
        // Only one endpoint light id (specific for that endpoint!) can exist for a single endpoint.
        //primaryKeys =
        //        {"endpointLightId", "endpointId"},
        //indices =
        //@Index(value = {"endpointId", "endpointLightId"},
        //        unique = true),
        // A BaseLight is always bound to a single endpoint. It cannot exist without one:
        // Therefore Room is instructed to delete this BaseLight if the endpoint gets deleted.
        foreignKeys =
        @ForeignKey(entity = EndpointConfig.class,
                parentColumns = "endpointId",
                childColumns = "endpointId",
                onDelete = ForeignKey.CASCADE))
public class BaseLight implements Light {

    static final String TABLENAME = "light";

    /**
     * Unique ID for a single BaseLight. Should not be set manually as it is automatically generated
     * by Room library. This is the single primary key for BaseLights.
     */
    @PrimaryKey
    @ColumnInfo(name = "lightId")
    public int id;

    /**
     * Foreign key of the remote endpoint that this BaseLight belongs to.
     */
    @ColumnInfo(name = "endpointId")
    private int endpointId;

    /**
     * Id for this BaseLight inside (!) the remote endpoint. This field helps the remote endpoint
     * to identify the correct BaseLight. It should not be confused with the primary key id.
     */
    @ColumnInfo(name = "endpointLightId")
    private int endpointLightId;

    @ColumnInfo(name = "friendlyName")
    public String friendlyName = "";

    @ColumnInfo(name = "isSwitchable")
    public boolean switchable = false;
    @ColumnInfo(name = "on")
    protected boolean on;

    @ColumnInfo(name = "isDimmable")
    public boolean dimmable = false;
    @ColumnInfo(name = "brightness")
    private int brightness; //TODO: Create contract for allowed values.

    @ColumnInfo(name = "isTemperaturable")
    public boolean temperaturable = false;
    @ColumnInfo(name = "colorTemperature")
    private int colorTemperature; //TODO: Create contract for allowed values.

    @ColumnInfo(name = "isColorable")
    public boolean colorable = false;
    @ColumnInfo(name = "color")
    private int color; //TODO: Create contract for allowed values.

    @Ignore
    private transient LightEndpoint endpoint;

    public BaseLight(int endpointId, int endpointLightId, String friendlyName, boolean switchable, boolean dimmable, boolean temperaturable, boolean colorable) {
        this.endpointId = endpointId;
        this.endpointLightId = endpointLightId;

        this.friendlyName = friendlyName;

        this.switchable = switchable;
        this.dimmable = dimmable;
        this.temperaturable = temperaturable;
        this.colorable = colorable;
    }

    @Ignore
    public BaseLight(int endpointId, int endpointLightId) {
        this.endpointId = endpointId;
        this.endpointLightId = endpointLightId;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEndpointLightId() {
        return endpointLightId;
    }

    public void setEndpointLightId(int endpointLightId) {
        this.endpointLightId = endpointLightId;
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

    @Override
    public void requestSetOn(boolean on) {
        endpoint.requestSetOn(this,on);
    }

    public void setOn(boolean on){
        this.on = on;
    }

    public int getBrightness() {
        return this.brightness;
    }

    @Override
    public void requestSetBrightness(int brightness) {
        endpoint.requestSetBrightness(this,brightness);
    }

    public void setBrightness(int brightness){
        this.brightness = brightness;
    }

    public int getColorTemperature() {
        return this.colorTemperature;
    }

    @Override
    public void requestSetColorTemperature(int colorTemperature) {
        endpoint.requestSetColorTemperature(this, colorTemperature);
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

    public int getEndpointId() {
        return this.endpointId;
    }

    public void setEndpointId(int endpointId){
        this.endpointId = endpointId;
    }

}
