package org.asdfgamer.sunriseClock.utils;

import androidx.annotation.NonNull;

public enum SettingKeys {
    ACTIVATED_LIGHTS("activatedLights"),
    ;

    String name;

    SettingKeys(String string) {
        name = string;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
