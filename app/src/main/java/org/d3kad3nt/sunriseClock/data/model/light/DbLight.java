package org.d3kad3nt.sunriseClock.data.model.light;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import org.d3kad3nt.sunriseClock.data.model.DbEndpointEntity;
import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointConfig;
import org.jetbrains.annotations.Contract;

@Entity(tableName = DbLight.TABLENAME,
    indices = {@Index(value = {"endpoint_id", "id_on_endpoint"},
        unique = true)},
    // A DbLight is always bound to a single endpoint. It cannot exist without one:
    // Therefore Room is instructed to delete this DbLight if the endpoint gets deleted.
    foreignKeys = @ForeignKey(entity = EndpointConfig.class,
        parentColumns = "endpointId",
        childColumns = "endpoint_id",
        onDelete = ForeignKey.CASCADE))
public class DbLight extends DbEndpointEntity {

    @Ignore
    public static final String TABLENAME = "light";

    @Ignore
    static final int BRIGHTNESS_MIN = 0;
    @Ignore
    static final int BRIGHTNESS_MAX = 100;

    @Ignore
    private static final String TAG = "DbLight";

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
     * Create a new object that represents a light in the app's Room database. This constructor has to be public for
     * Room to be able to create an object. This should not be otherwise accessed!
     */
    public DbLight(long endpointId, String endpointObjectId, String name, boolean isSwitchable, boolean isOn,
                   boolean isDimmable, int brightness, boolean isTemperaturable, int colorTemperature,
                   boolean isColorable, int color) {

        super(endpointId, endpointObjectId, name);

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
    }

    @NonNull
    @Contract("_ -> new")
    public static DbLight from(@NonNull RemoteLight remoteLight) {
        return RemoteLight.toDbLight(remoteLight);
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
}
