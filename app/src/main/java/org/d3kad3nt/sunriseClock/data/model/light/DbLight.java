package org.d3kad3nt.sunriseClock.data.model.light;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointConfig;
import org.d3kad3nt.sunriseClock.util.LogUtil;
import org.jetbrains.annotations.Contract;

@Entity(tableName = DbLight.TABLENAME,
        indices = {@Index(value = {"endpoint_id", "endpoint_light_id"}, unique = true)},
        // A DbLight is always bound to a single endpoint. It cannot exist without one:
        // Therefore Room is instructed to delete this DbLight if the endpoint gets deleted.
        foreignKeys = @ForeignKey(entity = EndpointConfig.class, parentColumns = "endpointId",
                                  childColumns = "endpoint_id", onDelete = ForeignKey.CASCADE))
public class DbLight {

    @Ignore
    public static final String TABLENAME = "light";
    @Ignore
    static final int BRIGHTNESS_MIN = 0;
    @Ignore
    static final int BRIGHTNESS_MAX = 100;
    @ColumnInfo(name = "endpoint_id")
    private final long endpointId;
    @ColumnInfo(name = "endpoint_light_id")
    @NonNull
    // Set SQLITE notNull attribute, for primitive types this is set automatically (but this is a string).
    private final String endpointLightId;
    @ColumnInfo(name = "name", defaultValue = "No Name")
    @NonNull
    // Set SQLITE notNull attribute, for primitive types this is set automatically (but this is a string).
    private final String name;
    @ColumnInfo(name = "is_switchable", defaultValue = "false")
    private final boolean isSwitchable;
    @ColumnInfo(name = "is_on", defaultValue = "false")
    private final boolean isOn;
    @ColumnInfo(name = "is_dimmable", defaultValue = "false")
    private final boolean isDimmable;
    @ColumnInfo(name = "brightness", defaultValue = "0")
    private final int brightness;
    @ColumnInfo(name = "is_temperaturable", defaultValue = "false")
    private final boolean isTemperaturable;
    @ColumnInfo(name = "colortemperature", defaultValue = "0")
    private final int colorTemperature;
    @ColumnInfo(name = "is_colorable", defaultValue = "false")
    private final boolean isColorable;
    @ColumnInfo(name = "color", defaultValue = "0")
    private final int color;

    @ColumnInfo(name = "is_reachable", defaultValue = "true")
    private final boolean isReachable;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "light_id")
    // Cannot be final because Room must be able to set the lightId after it was auto-generated.
    // Not set via DbLightBuilder.
    private long lightId;

    /**
     * Create a new object that represents a light in the app's Room database. This constructor has to be public for
     * Room to be able to create an object. This should not be otherwise accessed!
     */
    public DbLight(long endpointId, @NonNull String endpointLightId, @NonNull String name, boolean isSwitchable,
                   boolean isOn, boolean isDimmable, int brightness, boolean isTemperaturable, int colorTemperature,
                   boolean isColorable, int color, boolean isReachable) {
        if (endpointId != 0L) {
            this.endpointId = endpointId;
        } else {
            LogUtil.e("The given endpointId cannot be 0!");
            throw new IllegalArgumentException("The given endpointId cannot be 0!");
        }

        if (!endpointLightId.isEmpty()) {
            this.endpointLightId = endpointLightId;
        } else {
            LogUtil.e("The given endpointLightId string cannot be null or empty!");
            throw new IllegalArgumentException("The given endpointLightId string cannot be null or empty!");
        }

        this.name = name;
        this.isSwitchable = isSwitchable;
        this.isOn = isOn;
        this.isDimmable = isDimmable;

        if (brightness >= BRIGHTNESS_MIN && brightness <= BRIGHTNESS_MAX) {
            this.brightness = brightness;
        } else {
            throw new IllegalArgumentException("The given brightness of a light must be between 0 and 100!");
        }

        this.isTemperaturable = isTemperaturable;
        this.colorTemperature = colorTemperature; //Todo: Define which values are allowed, see DbLightBuilder javadoc
        this.isColorable = isColorable;
        this.color = color; //Todo: Define which values are allowed, see DbLightBuilder javadoc
        this.isReachable = isReachable;
    }

    @NonNull
    @Contract("_ -> new")
    public static DbLight from(@NonNull RemoteLight remoteLight) {
        return RemoteLight.toDbLight(remoteLight);
    }

    /**
     * @return Identifier for this light (inside the database).
     */
    public long getLightId() {
        return lightId;
    }

    /**
     * This setter has to be public for Room to be able to set the auto-generated id. It must not be used outside of
     * Room!
     *
     * @param lightId The auto-generated identifier of this light, not depending on the (endpoint-specific)
     *                endpointLightId.
     */
    public void setLightId(long lightId) {
        this.lightId = lightId;
    }

    /**
     * @return Foreign key (Room/SQLite) of the remote endpoint that this light belongs to. Only one endpoint light id
     * (specific for that endpoint!) can exist for a single endpoint.
     */
    public long getEndpointId() {
        return endpointId;
    }

    /**
     * This field enables the remote endpoint to identify the correct light. A remote endpoint cannot work with the
     * lightId.
     *
     * @return Identifier for this light inside (!) the remote endpoint.
     */
    @NonNull
    public String getEndpointLightId() {
        return endpointLightId;
    }

    /**
     * @return Name that can be used by the user to identify this light.
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * @return Whether the light's capabilities allow it to be turned on and off (true if allowed by the device, false
     * if not).
     */
    public boolean getIsSwitchable() {
        return isSwitchable;
    }

    /**
     * @return Whether the light is currently switched on (true) or off (false).
     */
    public boolean getIsOn() {
        return isOn;
    }

    /**
     * @return Whether the light's capabilities allow it to be dimmed (true if allowed by the device, false if not).
     */
    public boolean getIsDimmable() {
        return isDimmable;
    }

    /**
     * @return The current brightness of the light, where 0 is the lowest brightness or off (depending on the light)
     * and 100 is the highest brightness.
     */
    @IntRange(from = 0, to = 100)
    public int getBrightness() {
        return brightness;
    }

    /**
     * @return Whether the light's capabilities allow its color temperature to be changed (true if allowed by the
     * device, false if not).
     */
    public boolean getIsTemperaturable() {
        return isTemperaturable;
    }

    // Todo: Add javadoc to document allowed values for the color temperature.
    public int getColorTemperature() {
        return colorTemperature;
    }

    /**
     * @return Whether the light's capabilities allow its color to be changed (true if allowed by the device, false if
     * not).
     */
    public boolean getIsColorable() {
        return isColorable;
    }

    // Todo: Add javadoc to document allowed values for the color.
    public int getColor() {
        return color;
    }

    public boolean getIsReachable() {
        return isReachable;
    }
}