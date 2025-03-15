package org.d3kad3nt.sunriseClock.data.model.light;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

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
            return new UILightChangePayload.LightId(newItem.getLightId());
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

    public interface UILightChangePayload {

        void bindVariable(UpdateBinderVariable updateBinderVariable);

        class LightId implements UILightChangePayload {

            public final long lightId;

            LightId(long lightId) {
                this.lightId = lightId;
            }

            @Override
            public void bindVariable(final UpdateBinderVariable updateBinderVariable) {

                throw new UnsupportedOperationException("Not yet implemented");
            }
        }

        class EndpointId implements UILightChangePayload {

            public final long endpointId;

            EndpointId(long endpointId) {
                this.endpointId = endpointId;
            }

            @Override
            public void bindVariable(final UpdateBinderVariable updateBinderVariable) {

                throw new UnsupportedOperationException("Not yet implemented");
            }
        }

        class LightName implements UILightChangePayload {

            public final String lightName;

            LightName(String lightName) {
                this.lightName = lightName;
            }

            @Override
            public void bindVariable(final UpdateBinderVariable updateBinderVariable) {

                throw new UnsupportedOperationException("Not yet implemented");
            }
        }

        class LightIsSwitchable implements UILightChangePayload {

            public final boolean isSwitchable;

            LightIsSwitchable(boolean isSwitchable) {
                this.isSwitchable = isSwitchable;
            }

            @Override
            public void bindVariable(final UpdateBinderVariable updateBinderVariable) {

                throw new UnsupportedOperationException("Not yet implemented");
            }
        }

        class LightOn implements UILightChangePayload {

            public final boolean isOn;

            LightOn(boolean isOn) {
                this.isOn = isOn;
            }

            @Override
            public void bindVariable(final UpdateBinderVariable updateBinderVariable) {

                throw new UnsupportedOperationException("Not yet implemented");
            }
        }

        class LightIsDimmable implements UILightChangePayload {

            public final boolean isDimmable;

            LightIsDimmable(boolean isDimmable) {
                this.isDimmable = isDimmable;
            }

            @Override
            public void bindVariable(final UpdateBinderVariable updateBinderVariable) {

                throw new UnsupportedOperationException("Not yet implemented");
            }
        }

        class LightBrightness implements UILightChangePayload {

            public final int brightness;

            LightBrightness(int brightness) {
                this.brightness = brightness;
            }

            @Override
            public void bindVariable(final UpdateBinderVariable updateBinderVariable) {

                throw new UnsupportedOperationException("Not yet implemented");
            }
        }

        class LightIsTemperaturable implements UILightChangePayload {

            public final boolean isTemperaturable;

            LightIsTemperaturable(boolean isTemperaturable) {
                this.isTemperaturable = isTemperaturable;
            }

            @Override
            public void bindVariable(final UpdateBinderVariable updateBinderVariable) {
                throw new UnsupportedOperationException("Not yet implemented");
            }
        }

        class LightIsColorable implements UILightChangePayload {

            public final boolean isColorable;

            LightIsColorable(boolean isColorable) {
                this.isColorable = isColorable;
            }

            @Override
            public void bindVariable(final UpdateBinderVariable updateBinderVariable) {
                throw new UnsupportedOperationException("Not yet implemented");
            }
        }

        class LightIsReachable implements UILightChangePayload {

            public final boolean isReachable;

            LightIsReachable(boolean isReachable) {
                this.isReachable = isReachable;
            }

            @Override
            public void bindVariable(final UpdateBinderVariable updateBinderVariable) {
                throw new UnsupportedOperationException("Not yet implemented");
            }
        }
    }
}
