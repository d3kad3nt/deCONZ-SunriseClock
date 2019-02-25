package org.asdfgamer.sunriseClock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.asdfgamer.sunriseClock.network.utils.DeconzRequestQueue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SharedPreferences preferences;

    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.inflateMenu(R.menu.toolbar);
        setSupportActionBar(toolbar);


        DeconzRequestQueue.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                showPreferences();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void syncAlarm(View view)
    {
        Intent startIntent = new Intent("AlarmReact");
        PendingIntent startPIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, startIntent, 0);
        AlarmManager alarm = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        //alarm.set(AlarmManager.RTC_WAKEUP, triggerTime, startPIntent);
    }

    private void showPreferences() {
        Intent showPreferences = new Intent(this, PreferencesActivity.class);
        startActivity(showPreferences);
    }

    public static Context getContext() {
        return instance;
    }
}
