package org.asdfgamer.sunriseClock.utils;

import androidx.annotation.NonNull;

public enum Settings {
    ACTIVATED_LIGHTS("activatedLights"),
    ;

    String name;

    Settings(String string) {
        name = string;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
