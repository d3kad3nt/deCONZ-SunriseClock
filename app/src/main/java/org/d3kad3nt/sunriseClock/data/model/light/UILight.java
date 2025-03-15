package org.d3kad3nt.sunriseClock.data.model.light;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;

import org.d3kad3nt.sunriseClock.util.LogUtil;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

import kotlin.jvm.functions.Function2;

public class UILight {

    private final long lightId;
    private final long endpointId;
    // endpointLightId not needed in UI

    private final String name;

    private final boolean isSwitchable;
    private final boolean isOn;

    private final boolean isDimmable;
    private final int brightness;

    private final boolean isTemperaturable;
    //private final int colorTemperature; // Not yet implemented in the backend

    private final boolean isColorable;
    //private final int color; // Not yet implemented in the backend

    private final boolean isReachable;

    private UILight(long lightId, long endpointId, String name, boolean isSwitchable, boolean isOn,
                    boolean isDimmable, @IntRange(from = 0, to = 100) int brightness,
                    boolean isTemperaturable/*, int colorTemperature*/, boolean isColorable/*, int color*/,
                    boolean isReachable) {
        this.lightId = lightId;
        this.endpointId = endpointId;
        this.name = name;
        this.isSwitchable = isSwitchable;
        this.isOn = isOn;
        this.isDimmable = isDimmable;
        this.brightness = brightness;
        this.isTemperaturable = isTemperaturable;
        //this.colorTemperature = colorTemperature;
        this.isColorable = isColorable;
        //this.color = color;
        this.isReachable = isReachable;
    }

    @NonNull
    @Contract("_ -> new")
    public static UILight from(@NonNull DbLight dbLight) {
        // Place for conversion logic (if UI needs other data types or value ranges).
        UILight uiLight =
            new UILight(dbLight.getLightId(), dbLight.getEndpointId(), dbLight.getName(), dbLight.getIsSwitchable(),
                dbLight.getIsOn(), dbLight.getIsDimmable(), dbLight.getBrightness(), dbLight.getIsTemperaturable(),
                dbLight.getIsColorable(), dbLight.getIsReachable());
        LogUtil.v("Converted DbLight with lightId %d (endpointId %d, endpointLightId %s) to UILight.",
            dbLight.getLightId(), dbLight.getEndpointId(), dbLight.getEndpointLightId());
        return uiLight;
    }

    public static UILightChangePayload getSingleChangePayload(@NonNull UILight oldItem, @NonNull UILight newItem) {
        if (!Objects.equals(oldItem.getLightId(), newItem.getLightId())) {
            return new UILightChangePayload(UILightChangePayload.Type.lightId, newItem.getLightId());
        } else if (!Objects.equals(oldItem.getEndpointId(), newItem.getEndpointId())) {
            return new UILightChangePayload(UILightChangePayload.Type.endpointId, newItem.getEndpointId());
        } else if (!Objects.equals(oldItem.getName(), newItem.getName())) {
            return new UILightChangePayload(UILightChangePayload.Type.lightName, newItem.getName());
        } else if (!Objects.equals(oldItem.getIsSwitchable(), newItem.getIsSwitchable())) {
            return new UILightChangePayload(UILightChangePayload.Type.lightIsSwitchable, newItem.getIsSwitchable());
        } else if (!Objects.equals(oldItem.getIsOn(), newItem.getIsOn())) {
            return new UILightChangePayload(UILightChangePayload.Type.lightIsOn, newItem.getIsOn());
        } else if (!Objects.equals(oldItem.getIsDimmable(), newItem.getIsDimmable())) {
            return new UILightChangePayload(UILightChangePayload.Type.lightIsDimmable, newItem.getIsDimmable());
        } else if (!Objects.equals(oldItem.getBrightness(), newItem.getBrightness())) {
            return new UILightChangePayload(UILightChangePayload.Type.lightBrightness, newItem.getBrightness());
        } else if (!Objects.equals(oldItem.getIsTemperaturable(), newItem.getIsTemperaturable())) {
            return new UILightChangePayload(UILightChangePayload.Type.lightIsTemperaturable,
                newItem.getIsTemperaturable());
        } else if (!Objects.equals(oldItem.getIsColorable(), newItem.getIsColorable())) {
            return new UILightChangePayload(UILightChangePayload.Type.lightIsColorable, newItem.getIsColorable());
        } else if (!Objects.equals(oldItem.getIsReachable(), newItem.getIsReachable())) {
            return new UILightChangePayload(UILightChangePayload.Type.lightIsReachable, newItem.getIsReachable());
        }
        return null;
    }

    public long getLightId() {
        return lightId;
    }

    public long getEndpointId() {
        return endpointId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        UILight otherLight = (UILight) o;
        return Objects.equals(lightId, otherLight.lightId) && Objects.equals(endpointId, otherLight.endpointId) &&
            Objects.equals(name, otherLight.name) && Objects.equals(isSwitchable, otherLight.isSwitchable) &&
            Objects.equals(isOn, otherLight.isOn) && Objects.equals(isDimmable, otherLight.isDimmable) &&
            Objects.equals(brightness, otherLight.brightness) &&
            Objects.equals(isTemperaturable, otherLight.isTemperaturable) &&
            Objects.equals(isColorable, otherLight.isColorable) &&
            Objects.equals(isReachable, otherLight.isReachable);
    }

    public interface UpdateBinderVariable extends Function2<Integer, Object, Boolean> {}

    public static class UILightChangePayload {

        private final Type type;
        private final Object value;

        UILightChangePayload(Type type, Object value) {
            this.type = type;
            this.value = value;
        }

        public void bindVariable(UpdateBinderVariable updateBinderVariable) {
            if (type.id != null) {
                updateBinderVariable.invoke(type.id, value);
            } else {
                LogUtil.d("Changing of the %s value isn't supported yet", type.name());
            }
        }

        public Type getType() {
            return type;
        }

        public enum Type {
            endpointId(null),
            lightId(null),
            lightIsSwitchable(BR.lightIsSwitchable),
            lightIsOn(BR.lightIsOn),
            lightIsDimmable(BR.lightIsDimmable),
            lightBrightness(BR.lightBrightness),
            lightIsTemperaturable(null),
            lightIsColorable(null),
            lightIsReachable(BR.lightIsReachable),
            lightName(BR.lightName);

            @Nullable
            private final Integer id;

            Type(@Nullable final Integer id) {
                this.id = id;
            }
        }
    }
}
