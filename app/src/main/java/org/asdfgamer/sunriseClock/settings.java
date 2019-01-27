package org.asdfgamer.sunriseClock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.asdfgamer.sunriseClock.ui.settings.SettingsFragment;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SettingsFragment.newInstance())
                    .commitNow();
        }
    }
}
