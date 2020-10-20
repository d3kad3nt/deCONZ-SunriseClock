package org.d3kad3nt.sunriseClock.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import me.ibrahimsn.library.LivePreference;
import me.ibrahimsn.library.LiveSharedPreferences;

public class SettingsRepository {

    private static final String TAG = "SettingsRepository";
    private static volatile SettingsRepository INSTANCE;
    private final LiveSharedPreferences liveSharedPreferences;
    private final SharedPreferences preferences;


    private SettingsRepository(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        liveSharedPreferences = new LiveSharedPreferences(preferences);
    }

    public static SettingsRepository getInstance(Context context){
        if (INSTANCE == null) {
            synchronized (LightRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SettingsRepository(context);
                }
            }
        }
        return INSTANCE;
    }

    public LivePreference<Boolean> getBooleanSetting(String id){
        if (!preferences.contains(id)){
            Log.i(TAG, "The Setting " + id + " doesn't exist yet.");
        };
        return liveSharedPreferences.getBoolean(id,false);
    }

    public LivePreference<Integer> getIntegerSetting(String id, int defaultValue) {
        if (!preferences.contains(id)){
            Log.i(TAG, "The Setting " + id + " doesn't exist yet.");
        };
        return liveSharedPreferences.getInt(id,defaultValue);
    }

    public LivePreference<Long> getLongSetting(String id, long defaultValue) {
        if (!preferences.contains(id)){
            Log.i(TAG, "The Setting " + id + " doesn't exist yet.");
        };
        return liveSharedPreferences.getLong(id,defaultValue);
    }

    public void setSetting(String id, long value) {
        preferences.edit().putLong(id,value).apply();
    }
}
