package org.d3kad3nt.sunriseClock.backend.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Supplier;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class SettingsRepository {

    private static volatile SettingsRepository INSTANCE;
    private final SharedPreferences preferences;
    private final Map<SettingKeys, Listener<Boolean>> listenerBooleanCache = new HashMap<>();
    private final Map<SettingKeys, Listener<Long>> listenerLongCache = new HashMap<>();
    private final Map<SettingKeys, Listener<Integer>> listenerIntCache = new HashMap<>();
    private final Map<SettingKeys, Listener<String>> listenerStringCache = new HashMap<>();

    private SettingsRepository(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

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

    public LiveData<Optional<Long>> getActiveEndpointIdAsLivedata() {
        return getObservableLongSetting(SettingKeys.ACTIVE_ENDPOINT_ID);
    }

    public long getActiveEndpoint() {
        return getLongSetting(SettingKeys.ACTIVE_ENDPOINT_ID);
    }

    public void setActiveEndpoint(long value) {
        LogUtil.i("Set active Endpoint to id %d", value);
        setLongSetting(SettingKeys.ACTIVE_ENDPOINT_ID, value);
    }

    private boolean getBooleanSetting(SettingKeys key) {
        validateSettingExists(key);
        return preferences.getBoolean(key.toString(), false);
    }

    private long getLongSetting(SettingKeys key) {
        validateSettingExists(key);
        return preferences.getLong(key.toString(), 0);
    }

    private int getIntSetting(SettingKeys key) {
        validateSettingExists(key);
        return preferences.getInt(key.toString(), 0);
    }

    private String getStringSetting(SettingKeys key) {
        validateSettingExists(key);
        return preferences.getString(key.toString(), "");
    }

    private void validateSettingExists(@NonNull final SettingKeys key) {
        if (!preferences.contains(key.toString())) {
            throw new IllegalStateException(String.format("Setting %s doesn't exists", key));
        }
    }

    private <T> LiveData<Optional<T>> getObservableSetting(
            SettingKeys settingKeys,
            @NonNull Map<SettingKeys, Listener<T>> cache,
            @NonNull final Supplier<T> function) {
        if (cache.containsKey(settingKeys)) {
            return Objects.requireNonNull(cache.get(settingKeys)).liveData();
        }
        Optional<T> initialValue;
        if (preferences.contains(settingKeys.toString())) {
            initialValue = Optional.of(function.get());
        } else {
            initialValue = Optional.empty();
        }
        MutableLiveData<Optional<T>> liveData = new MutableLiveData<>(initialValue);
        SharedPreferences.OnSharedPreferenceChangeListener listener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(
                            final SharedPreferences sharedPreferences, @Nullable final String key) {
                        if (key != null && key.equals(settingKeys.toString())) {
                            liveData.postValue(Optional.of(function.get()));
                        }
                    }
                };
        cache.put(settingKeys, new Listener<>(listener, liveData));
        preferences.registerOnSharedPreferenceChangeListener(listener);
        return liveData;
    }

    private LiveData<Optional<Long>> getObservableLongSetting(SettingKeys settingKeys) {
        return getObservableSetting(settingKeys, listenerLongCache, new Supplier<>() {
            public Long get() {
                return preferences.getLong(settingKeys.toString(), 0);
            }
        });
    }

    private LiveData<Optional<Boolean>> getObservableBooleanSetting(SettingKeys settingKeys) {
        return getObservableSetting(settingKeys, listenerBooleanCache, () -> {
            return preferences.getBoolean(settingKeys.toString(), false);
        });
    }

    private LiveData<Optional<String>> getObservableStringSetting(SettingKeys settingKeys) {
        return getObservableSetting(settingKeys, listenerStringCache, () -> {
            return preferences.getString(settingKeys.toString(), "");
        });
    }

    private LiveData<Optional<Integer>> getObservableIntSetting(SettingKeys settingKeys) {
        return getObservableSetting(settingKeys, listenerIntCache, () -> {
            return preferences.getInt(settingKeys.toString(), 0);
        });
    }

    private void setLongSetting(@NonNull SettingKeys key, long value) {
        preferences.edit().putLong(key.toString(), value).apply();
    }

    private void setIntSetting(@NonNull SettingKeys key, int value) {
        preferences.edit().putInt(key.toString(), value).apply();
    }

    private void setStringSetting(@NonNull SettingKeys key, String value) {
        preferences.edit().putString(key.toString(), value).apply();
    }

    private void setBooleanSetting(@NonNull SettingKeys key, Boolean value) {
        preferences.edit().putBoolean(key.toString(), value).apply();
    }

    enum SettingKeys {
        ACTIVE_ENDPOINT_ID("pref_active_endpoint_id");

        final String name;

        SettingKeys(String string) {
            name = string;
        }

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }

    record Listener<T>(
            SharedPreferences.OnSharedPreferenceChangeListener listener, MutableLiveData<Optional<T>> liveData) {}
}
