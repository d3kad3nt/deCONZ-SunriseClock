package org.d3kad3nt.sunriseClock.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

public class DefaultPreferences {
    private final Context context;

    private final String TAG = "DefaultPreferences";

    public DefaultPreferences(Context applicationContext) {
        this.context = applicationContext;
    }

    public void apply() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        initPreference(SettingKeys.ALARM_ACTIVE, true, preferences);
        initPreference(SettingKeys.IP, "localhost", preferences);
        initPreference(SettingKeys.PORT, "80", preferences);
    }

    private void initPreference(SettingKeys setting, boolean value, SharedPreferences preferences) {
        if (!preferences.contains(setting.toString())) {
            Log.i(TAG, "Set default value for " + setting + " to " + value);
            preferences.edit().putBoolean(setting.toString(), value).apply();
        }
    }

    private void initPreference(SettingKeys setting, String value, SharedPreferences preferences) {
        if (!preferences.contains(setting.toString())) {
            Log.i(TAG, "Set default value for " + setting + " to " + value);
            preferences.edit().putString(setting.toString(), value).apply();
        }
    }
}
