package org.asdfgamer.sunriseClock;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class Settings {

    private SharedPreferences settings;

    public Settings(Context applicationContext)
    {
        settings = applicationContext.getSharedPreferences(applicationContext.getString(R.string.app_name), 0);
    }
    
    public void save(ID id, String value)
    {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(id.toString(), value);
        editor.apply();
    }

    public void save(ID id, int value)
    {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(id.toString(), value);
        editor.apply();
    }

    public void save(ID id, boolean value)
    {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(id.toString(), value);
        editor.apply();
    }

    public int loadInt(ID id, int defaultValue)
    {
        return settings.getInt(id.toString(), defaultValue);
    }

    public boolean loadBoolean(ID id, boolean defaultValue)
    {
        return settings.getBoolean(id.toString(), defaultValue);
    }

    public String loadString(ID id, String defaultValue)
    {
        return settings.getString(id.toString(), defaultValue);
    }

    public enum ID
    {
        ip("ip"),
        apiKey("apiKey"),
        url("url"),
        id("id"),
        ;

        private final String name;
        ID(String name)
        {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
