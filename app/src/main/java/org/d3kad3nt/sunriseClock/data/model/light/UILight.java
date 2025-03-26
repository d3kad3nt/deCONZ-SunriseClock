package org.d3kad3nt.sunriseClock.data.model.light;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.d3kad3nt.sunriseClock.data.model.ListItemType;
import org.d3kad3nt.sunriseClock.data.model.UIEndpointEntity;
import org.d3kad3nt.sunriseClock.util.LogUtil;
import org.jetbrains.annotations.Contract;

public class UILight extends UIEndpointEntity<UILight> {

    private final boolean isSwitchable;
    private final boolean isOn;

    private final boolean isDimmable;
    private final int brightness;

    private final boolean isTemperaturable;
    // private final int colorTemperature; // Not yet implemented in the backend

    private final boolean isColorable;
    // private final int color; // Not yet implemented in the backend

    private final boolean isReachable;

    private UILight(
            long lightId,
            long endpointId,
            String name,
            boolean isSwitchable,
            boolean isOn,
            boolean isDimmable,
            @IntRange(from = 0, to = 100) int brightness,
            boolean isTemperaturable /*, int colorTemperature*/,
            boolean isColorable /*, int color*/,
            boolean isReachable) {
        super(lightId, endpointId, name);

        this.isSwitchable = isSwitchable;
        this.isOn = isOn;
        this.isDimmable = isDimmable;
        this.brightness = brightness;
        this.isTemperaturable = isTemperaturable;
        // this.colorTemperature = colorTemperature;
        this.isColorable = isColorable;
        // this.color = color;
        this.isReachable = isReachable;
    }

    @NonNull
    @Contract("_ -> new")
    public static UILight from(@NonNull DbLight dbLight) {
        // Place for conversion logic (if UI needs other data types or value ranges).
        UILight uiLight = new UILight(
                dbLight.getId(),
                dbLight.getEndpointId(),
                dbLight.getName(),
                dbLight.getIsSwitchable(),
                dbLight.getIsOn(),
                dbLight.getIsDimmable(),
                dbLight.getBrightness(),
                dbLight.getIsTemperaturable(),
                dbLight.getIsColorable(),
                dbLight.getIsReachable());
        LogUtil.v(
                "Converted DbLight with lightId %d (endpointId %d, endpointLightId %s) to UILight.",
                dbLight.getId(), dbLight.getEndpointId(), dbLight.getEndpointEntityId());
        return uiLight;
    }

    @NonNull
    @Contract("_ -> new")
    public static List<UILight> from(@NonNull List<DbLight> dbLights) {
        return dbLights.stream().map(dbLight -> from(dbLight)).collect(Collectors.toList());
    }

    public static UILightChangePayload getSingleChangePayload(@NonNull UILight oldItem, @NonNull UILight newItem) {
        if (!Objects.equals(oldItem.getId(), newItem.getId())) {
            return new UILightChangePayload.LightId(newItem.getId());
        } else if (!Objects.equals(oldItem.getEndpointId(), newItem.getEndpointId())) {
            return new UILightChangePayload.EndpointId(newItem.getEndpointId());
        } else if (!Objects.equals(oldItem.getName(), newItem.getName())) {
            return new UILightChangePayload.LightName(newItem.getName());
        } else if (!Objects.equals(oldItem.getIsSwitchable(), newItem.getIsSwitchable())) {
            return new UILightChangePayload.LightIsSwitchable(newItem.getIsSwitchable());
        } else if (!Objects.equals(oldItem.getIsOn(), newItem.getIsOn())) {
            return new UILightChangePayload.LightOn(newItem.getIsOn());
        } else if (!Objects.equals(oldItem.getIsDimmable(), newItem.getIsDimmable())) {
            return new UILightChangePayload.LightIsDimmable(newItem.getIsDimmable());
        } else if (!Objects.equals(oldItem.getBrightness(), newItem.getBrightness())) {
            return new UILightChangePayload.LightBrightness(newItem.getBrightness());
        } else if (!Objects.equals(oldItem.getIsTemperaturable(), newItem.getIsTemperaturable())) {
            return new UILightChangePayload.LightIsTemperaturable(newItem.getIsTemperaturable());
        } else if (!Objects.equals(oldItem.getIsColorable(), newItem.getIsColorable())) {
            return new UILightChangePayload.LightIsColorable(newItem.getIsColorable());
        } else if (!Objects.equals(oldItem.getIsReachable(), newItem.getIsReachable())) {
            return new UILightChangePayload.LightIsReachable(newItem.getIsReachable());
        }
        return null;
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

    @IntRange(from = 0, to = 100)
    public int getBrightness() {
        return brightness;
    }

    public boolean getIsTemperaturable() {
        return isTemperaturable;
    }

    public boolean getIsColorable() {
        return isColorable;
    }

    public boolean getIsReachable() {
        return isReachable;
    }

    public ListItemType getType() {
        return ListItemType.LIGHT;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                isSwitchable,
                isOn,
                isDimmable,
                brightness,
                isTemperaturable,
                isColorable,
                isReachable);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final UILight uiLight)) {
            return false;
        }
        return super.equals(o)
                && isSwitchable == uiLight.isSwitchable
                && isOn == uiLight.isOn
                && isDimmable == uiLight.isDimmable
                && brightness == uiLight.brightness
                && isTemperaturable == uiLight.isTemperaturable
                && isColorable == uiLight.isColorable
                && isReachable == uiLight.isReachable;
    }

    public interface UILightChangePayload {

        class LightId implements UILightChangePayload {

            public final long lightId;

            LightId(long lightId) {
                this.lightId = lightId;
            }
        }

        class EndpointId implements UILightChangePayload {

            public final long endpointId;

            EndpointId(long endpointId) {
                this.endpointId = endpointId;
            }
        }

        class LightName implements UILightChangePayload {

            public final String lightName;

            LightName(String lightName) {
                this.lightName = lightName;
            }
        }

        class LightIsSwitchable implements UILightChangePayload {

            public final boolean isSwitchable;

            LightIsSwitchable(boolean isSwitchable) {
                this.isSwitchable = isSwitchable;
            }
        }

        class LightOn implements UILightChangePayload {

            public final boolean isOn;

            LightOn(boolean isOn) {
                this.isOn = isOn;
            }
        }

        class LightIsDimmable implements UILightChangePayload {

            public final boolean isDimmable;

            LightIsDimmable(boolean isDimmable) {
                this.isDimmable = isDimmable;
            }
        }

        class LightBrightness implements UILightChangePayload {

            public final int brightness;

            LightBrightness(int brightness) {
                this.brightness = brightness;
            }
        }

        class LightIsTemperaturable implements UILightChangePayload {

            public final boolean isTemperaturable;

            LightIsTemperaturable(boolean isTemperaturable) {
                this.isTemperaturable = isTemperaturable;
            }
        }

        class LightIsColorable implements UILightChangePayload {

            public final boolean isColorable;

            LightIsColorable(boolean isColorable) {
                this.isColorable = isColorable;
            }
        }

        class LightIsReachable implements UILightChangePayload {

            public final boolean isReachable;

            LightIsReachable(boolean isReachable) {
                this.isReachable = isReachable;
            }
        }
    }
}
