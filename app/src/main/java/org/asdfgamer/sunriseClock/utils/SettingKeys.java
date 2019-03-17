package org.asdfgamer.sunriseClock.utils;

import androidx.annotation.NonNull;

public enum SettingKeys {
    ACTIVATED_LIGHTS("activatedLights"),
    ALARM_ACTIVE("pref_alarm_active"),
    IP("pref_ip"),
    PORT("pref_port");

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
