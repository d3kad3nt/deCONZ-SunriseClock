package org.d3kad3nt.sunriseClock.data.model.light;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import org.d3kad3nt.sunriseClock.data.model.DbEndpointEntity;
import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointConfig;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

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

    @ColumnInfo(name = "is_switchable",
        defaultValue = "false")
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

    /**
     * Create a new entity that represents a light in the app's Room database. This constructor has to be public for
     * Room to be able to create an object. This should not be otherwise accessed!
     */
    public DbLight(long endpointId, @NonNull String endpointEntityId, @NonNull String name, boolean isSwitchable,
                   boolean isOn, boolean isDimmable, int brightness, boolean isTemperaturable, int colorTemperature,
                   boolean isColorable, int color, boolean isReachable) {

        super(endpointId, endpointEntityId, name);

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

    @Override
    public String getTABLENAME() {
        return DbLight.TABLENAME;
    }

    // Room requires equals() and hashcode() to be implemented:
    // The key of the provided method's multimap return type must implement equals() and hashCode().
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final DbLight dbLight)) {
            return false;
        }
        return super.equals(dbLight) && (isSwitchable == dbLight.isSwitchable && isOn == dbLight.isOn && isDimmable == dbLight.isDimmable &&
            brightness == dbLight.brightness && isTemperaturable == dbLight.isTemperaturable &&
            colorTemperature == dbLight.colorTemperature && isColorable == dbLight.isColorable &&
            color == dbLight.color && isReachable == dbLight.isReachable);
    }

    // Room requires equals() and hashcode() to be implemented:
    // The key of the provided method's multimap return type must implement equals() and hashCode().
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isSwitchable, isOn, isDimmable, brightness, isTemperaturable,
            colorTemperature, isColorable, color, isReachable);
    }
}
