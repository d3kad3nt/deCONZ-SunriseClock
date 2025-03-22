package org.d3kad3nt.sunriseClock.data.model;

import androidx.annotation.NonNull;

public enum ListItemType {

    GROUP_HEADER,
    GROUP_FOOTER,
    LIGHT;

    @NonNull
    public static ListItemType valueOf(final int viewType) {
        for (var value : values()) {
            if (value.ordinal() == viewType) {
                return value;
            }
        }
        throw new IllegalArgumentException(String.format("Invalid ViewType %d", viewType));
    }
}
