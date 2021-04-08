package org.d3kad3nt.sunriseClock.model.light;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import org.d3kad3nt.sunriseClock.model.endpoint.EndpointConfig;



@Entity(tableName = BaseLight.TABLENAME,
        indices = {@Index("endpointId")},
        // Only one endpoint light id (specific for that endpoint!) can exist for a single endpoint.
        // This is suitable as a composite primary key.
        primaryKeys =
                {"endpointLightId", "endpointId"},
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
     * Foreign key of the remote endpoint that this BaseLight belongs to.
     * Only one endpoint light id (specific for that endpoint!) can exist for a single endpoint.
     *
     * This is suitable as part of a composite primary key.
     */
    @ColumnInfo(name = "endpointId")
    private long endpointId;

    /**
     * Id for this BaseLight inside (!) the remote endpoint. This field helps the remote endpoint
     * to identify the correct BaseLight.
     *
     * This is suitable as part of a composite primary key.
     */
    @NonNull
    @ColumnInfo(name = "endpointLightId")
    private String endpointLightId;

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
    private transient LightID lightID;

    public BaseLight(long endpointId, @NonNull String endpointLightId, String friendlyName, boolean switchable, boolean dimmable, boolean temperaturable, boolean colorable) {
        this.endpointId = endpointId;
        this.endpointLightId = endpointLightId;

        this.friendlyName = friendlyName;

        this.switchable = switchable;
        this.dimmable = dimmable;
        this.temperaturable = temperaturable;
        this.colorable = colorable;
    }

    @Ignore
    public BaseLight(long endpointId, @NonNull String endpointLightId) {
        this.endpointId = endpointId;
        this.endpointLightId = endpointLightId;
    }

    @Ignore
    public BaseLight(long endpointId) {
        this.endpointId = endpointId;
    }

    @NonNull
    public String getEndpointLightId() {
        return endpointLightId;
    }

    public void setEndpointLightId(@NonNull String endpointLightId) {
        this.endpointLightId = endpointLightId;
    }

//    public LightEndpoint getEndpoint(){
//        return this.endpoint;
//    }

    public boolean isOn() {
        return this.on;
    }

//    @Override
//    public void requestSetOn(boolean on) {
//        endpoint.requestSetOn(this,on);
//    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public int getBrightness() {
        return this.brightness;
    }

//    @Override
//    public void requestSetBrightness(int brightness) {
//        endpoint.requestSetBrightness(this,brightness);
//    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getColorTemperature() {
        return this.colorTemperature;
    }

//    @Override
//    public void requestSetColorTemperature(int colorTemperature) {
//        endpoint.requestSetColorTemperature(this, colorTemperature);
//    }

    public void setColorTemperature(int colorTemperature) {
        this.colorTemperature = colorTemperature;
    }

    public int getColor() {
        return this.color;
    }

//    @Override
//    public void requestSetColor(int color) {
//        endpoint.requestSetColor(this, color);
//    }

    public void setColor(int color) {
        this.color = color;
    }

    public long getEndpointId() {
        return this.endpointId;
    }

    public void setEndpointId(long endpointId) {
        this.endpointId = endpointId;
    }

    public LightID getUUID(){
        if (lightID == null){
            lightID = new LightID(endpointId, endpointLightId);
        }
        return lightID;
    }

}
