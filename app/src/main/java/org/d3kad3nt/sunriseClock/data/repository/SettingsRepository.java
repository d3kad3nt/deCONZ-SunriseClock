package org.d3kad3nt.sunriseClock.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SettingsRepository {

    private static final String TAG = "SettingsRepository";
    private static volatile SettingsRepository INSTANCE;
    private final SharedPreferences preferences;
    private final Map<SettingKeys, Listener<Boolean>> listenerBooleanCache = new HashMap<>();

    private SettingsRepository(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    record Listener<T>(SharedPreferences.OnSharedPreferenceChangeListener listener, MutableLiveData<T> liveData){}

    public static SettingsRepository getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LightRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SettingsRepository(context);
                }
            }
        }
        return INSTANCE;
    }

    public boolean getBooleanSetting(SettingKeys key){
        return false;
    }

    public LiveData<Long> getActiveEndpointIdAsLivedata(){
        return getObservableLongSetting(SettingKeys.ACTIVE_ENDPOINT_ID);
    }

    private LiveData<Boolean> getObservableBooleanSetting(SettingKeys id) {
        if (!preferences.contains(id.toString())) {
            throw new IllegalStateException(String.format("Setting %s doesn't exit", id));
        }
        if (listenerBooleanCache.containsKey(id)) {
            return Objects.requireNonNull(listenerBooleanCache.get(id)).liveData();
        }

        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences,
                                                  @Nullable final String key) {
                if (key != null && key.equals(id.toString())) {
                    liveData.postValue(sharedPreferences.getBoolean(key, false));
                }
            }
        };
        listenerBooleanCache.put(id, new Listener<>(listener, liveData));
        preferences.registerOnSharedPreferenceChangeListener(listener);
        return liveData;
    }


    public LivePreference<Integer> getIntegerSetting(String id, int defaultValue) {
        if (!preferences.contains(id)) {
            Log.i(TAG, "The Setting " + id + " doesn't exist yet.");
        }
        return liveSharedPreferences.getInt(id, defaultValue);
    }

    public LiveData<Long> getLongSetting(String id, long defaultValue) {
        if (!preferences.contains(id)) {
            Log.i(TAG, "The Setting " + id + " doesn't exist yet.");
        }
        return liveSharedPreferences.getLong(id, defaultValue);
    }

    public void setSetting(String id, long value) {
        preferences.edit().putLong(id, value).apply();
    }

    enum SettingKeys {
        ACTIVE_ENDPOINT_ID("pref_active_endpoint_id" );


        final String name;

        SettingKeys(String string ) {
            name = string;
        }

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }
}
