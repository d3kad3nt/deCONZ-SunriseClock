package org.d3kad3nt.sunriseClock.data.model.light;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointConfig;
import org.jetbrains.annotations.Contract;


@Entity(tableName = DbLight.TABLENAME,
        indices = {@Index( value = {"endpoint_id", "endpoint_light_id"}, unique = true)},
        // A DbLight is always bound to a single endpoint. It cannot exist without one:
        // Therefore Room is instructed to delete this DbLight if the endpoint gets deleted.
        foreignKeys =
        @ForeignKey(entity = EndpointConfig.class,
                parentColumns = "endpointId",
                childColumns = "endpoint_id",
                onDelete = ForeignKey.CASCADE))
public class DbLight {

    @Ignore
    public static final String TABLENAME = "light";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "light_id")
    // Cannot be final because Room must be able to set the lightId after it was auto-generated.
    // Not set via DbLightBuilder.
    private long lightId;

    @ColumnInfo(name = "endpoint_id")
    private final long endpointId;
    @ColumnInfo(name = "endpoint_light_id")
    @NonNull // Set SQLITE notNull attribute, for primitive types this is set automatically (but this is a string).
    private final String endpointLightId;

    @ColumnInfo(name = "name")
    @NonNull // Set SQLITE notNull attribute, for primitive types this is set automatically (but this is a string).
    private final String name;

    @ColumnInfo(name = "is_switchable")
    private final boolean isSwitchable;
    @ColumnInfo(name = "is_on")
    private final boolean isOn;

    @ColumnInfo(name = "is_dimmable")
    private final boolean isDimmable;
    @ColumnInfo(name = "brightness")
    private final int brightness;

    @ColumnInfo(name = "is_temperaturable")
    private final boolean isTemperaturable;
    @ColumnInfo(name = "colortemperature")
    private final int colorTemperature;

    @ColumnInfo(name = "is_colorable")
    private final boolean isColorable;
    @ColumnInfo(name = "color")
    private final int color;

    /**
     * Create a new object that represents a light in the app's Room database. This constructor has to be public for Room to be able to create an object. This should not be otherwise accessed!
     */
    public DbLight(long endpointId, String endpointLightId, String name, boolean isSwitchable, boolean isOn, boolean isDimmable, int brightness, boolean isTemperaturable, int colorTemperature, boolean isColorable, int color) {
        if (endpointId != 0L) {
            this.endpointId = endpointId;
        }
        else {
            throw new IllegalArgumentException("The given endpointId cannot be 0!");
        }

        if (endpointLightId != null && !endpointLightId.isEmpty()) {
            this.endpointLightId = endpointLightId;
        }
        else {
            throw new IllegalArgumentException("The given endpointLightId cannot be null or empty!");
        }

        this.name = name;
        this.isSwitchable = isSwitchable;
        this.isOn = isOn;
        this.isDimmable = isDimmable;

        if (brightness >= 0 && brightness <= 100) {
            this.brightness = brightness;
        }
        else {
            throw new IllegalArgumentException("The given brightness of a light must be between 0 and 100!");
        }

        this.isTemperaturable = isTemperaturable;
        this.colorTemperature = colorTemperature; //Todo: Define which values are allowed, see DbLightBuilder javadoc
        this.isColorable = isColorable;
        this.color = color; //Todo: Define which values are allowed, see DbLightBuilder javadoc
    }

    /**
     *
     * @return Identifier for this light (inside the database).
     */
    public long getLightId(){
        return lightId;
    }

    /**
     *
     * @return Foreign key (Room/SQLite) of the remote endpoint that this light belongs to. Only one endpoint light id (specific for that endpoint!) can exist for a single endpoint.
     */
    public long getEndpointId() {
        return endpointId;
    }

    /**
     * This field enables the remote endpoint to identify the correct light. A remote endpoint cannot work with the lightId.
     *
     * @return Identifier for this light inside (!) the remote endpoint.
     */
    @NonNull
    public String getEndpointLightId() {
        return endpointLightId;
    }

    /**
     *
     * @return Name that can be used by the user to identify this light.
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     *
     * @return Whether the light's capabilities allow it to be turned on and off (true if allowed by the device, false if not).
     */
    public boolean getIsSwitchable() {
        return isSwitchable;
    }

    /**
     *
     * @return Whether the light is currently switched on (true) or off (false).
     */
    public boolean getIsOn() {
        return isOn;
    }

    /**
     *
     * @return Whether the light's capabilities allow it to be dimmed (true if allowed by the device, false if not).
     */
    public boolean getIsDimmable() {
        return isDimmable;
    }

    /**
     *
     * @return The current brightness of the light, where 0 is the lowest brightness or off (depending on the light) and 100 is the highest brightness.
     */
    public int getBrightness() {
        return brightness;
    }

    /**
     *
     * @return Whether the light's capabilities allow its color temperature to be changed (true if allowed by the device, false if not).
     */
    public boolean getIsTemperaturable() {
        return isTemperaturable;
    }

    // Todo: Add javadoc to document allowed values for the color temperature.
    public int getColorTemperature() {
        return colorTemperature;
    }

    /**
     *
     * @return Whether the light's capabilities allow its color to be changed (true if allowed by the device, false if not).
     */
    public boolean getIsColorable() {
        return isColorable;
    }

    // Todo: Add javadoc to document allowed values for the color.
    public int getColor() {
        return color;
    }

    /**
     * This setter has to be public for Room to be able to set the auto-generated id. It must not be used outside of Room!
     *
     * @param lightId The auto-generated identifier of this light, not depending on the (endpoint-specific) endpointLightId.
     */
    public void setLightId(long lightId) {
        this.lightId = lightId;
    }

    @NonNull
    @Contract("_ -> new")
    public static DbLight from(@NonNull RemoteLight remoteLight) {
        return RemoteLight.toDbLight(remoteLight);
    }
}