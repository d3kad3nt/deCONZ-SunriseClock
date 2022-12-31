package org.d3kad3nt.sunriseClock.data.model.light;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointConfig;
import org.jetbrains.annotations.Contract;


@Entity(tableName = DbLight.TABLENAME,
        indices = {@Index( value = {"endpointId", "endpointLightId"}, unique = true)},
        // A DbLight is always bound to a single endpoint. It cannot exist without one:
        // Therefore Room is instructed to delete this DbLight if the endpoint gets deleted.
        foreignKeys =
        @ForeignKey(entity = EndpointConfig.class,
                parentColumns = "endpointId",
                childColumns = "endpointId",
                onDelete = ForeignKey.CASCADE))
public class DbLight {

    public static final String TABLENAME = "light";

    @PrimaryKey(autoGenerate = true)
    private long lightId;

    /**
     * Foreign key of the remote endpoint that this DbLight belongs to.
     * Only one endpoint light id (specific for that endpoint!) can exist for a single endpoint.
     */
    @ColumnInfo(name = "endpointId")
    private final long endpointId;
    /**
     * Id for this DbLight inside (!) the remote endpoint. This field helps the remote endpoint
     * to identify the correct DbLight.
     */
    @ColumnInfo(name = "endpointLightId")
    private final String endpointLightId;

    @ColumnInfo(name = "friendlyName")
    private final String name;

    @ColumnInfo(name = "isSwitchable")
    private final boolean isSwitchable;
    @ColumnInfo(name = "on")
    private final boolean isOn;

    @ColumnInfo(name = "isDimmable")
    private final boolean isDimmable;
    @ColumnInfo(name = "brightness")
    private final int brightness; //TODO: Create contract for allowed values.

    @ColumnInfo(name = "isTemperaturable")
    private final boolean isTemperaturable;
    @ColumnInfo(name = "colorTemperature")
    private final int colorTemperature; //TODO: Create contract for allowed values.

    @ColumnInfo(name = "isColorable")
    private final boolean isColorable;
    @ColumnInfo(name = "color")
    private final int color; //TODO: Create contract for allowed values.

    // Has to be public for Room to be able to create an object from the database's data
    public DbLight(long endpointId, String endpointLightId, String name, boolean isSwitchable, boolean isOn, boolean isDimmable, int brightness, boolean isTemperaturable, int colorTemperature, boolean isColorable, int color) {
        this.endpointId = endpointId;
        this.endpointLightId = endpointLightId;
        this.name = name;
        this.isSwitchable = isSwitchable;
        this.isOn = isOn;
        this.isDimmable = isDimmable;
        this.brightness = brightness;
        this.isTemperaturable = isTemperaturable;
        this.colorTemperature = colorTemperature;
        this.isColorable = isColorable;
        this.color = color;
    }

    public long getLightId(){
        return lightId;
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

    // Has to be public for Room to be able to set the (auto-generated) primary key
    public void setLightId(long lightId) {
        this.lightId = lightId;
    }

    @NonNull
    @Contract("_ -> new")
    public static DbLight from(@NonNull RemoteLight remoteLight) {
        //Todo: Logic to convert remote light to db light
        return new DbLight(remoteLight.getEndpointId(), remoteLight.getEndpointLightId(), remoteLight.getName(), remoteLight.getIsSwitchable(), remoteLight.getIsOn(), remoteLight.getIsDimmable(), remoteLight.getBrightness(), remoteLight.getIsTemperaturable(), remoteLight.getColorTemperature(), remoteLight.getIsColorable(), remoteLight.getColor());
    }
}