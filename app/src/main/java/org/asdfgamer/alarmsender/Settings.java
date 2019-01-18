package org.asdfgamer.alarmsender;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    private static final String SETTINGS_NAME = "AlarmSender";

    private SharedPreferences settings;

    public Settings(Context applicationContext)
    {
        settings = applicationContext.getSharedPreferences(SETTINGS_NAME, 0);
    }
    
    public void save(String id, String value)
    {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(id, value);
        editor.apply();
    }

    public void save(String id, int value)
    {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(id, value);
        editor.apply();
    }

    public void save(String id, boolean value)
    {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(id, value);
        editor.apply();
    }

    public int loadInt(String id, int defaultValue)
    {
        return settings.getInt(id, defaultValue);
    }

    public boolean loadBoolean(String id, boolean defaultValue)
    {
        return settings.getBoolean(id, defaultValue);
    }

    public String loadString(String id, String defaultValue)
    {
        return settings.getString(id,defaultValue);
    }

}
