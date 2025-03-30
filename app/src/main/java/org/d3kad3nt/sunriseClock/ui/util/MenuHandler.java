package org.d3kad3nt.sunriseClock.ui.util;

import android.view.MenuItem;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;

public interface MenuHandler {

    boolean onMenuClicked(@NonNull MenuItem menuItem);

    @MenuRes
    int getMenuId();
}
