package org.d3kad3nt.sunriseClock.backend.data.model;

import androidx.annotation.NonNull;

public enum ListItemType {
    HEADER,
    GROUP,
    LIGHT,
    FOOTER;

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
