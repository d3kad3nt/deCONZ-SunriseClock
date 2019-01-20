package org.asdfgamer.sunriseClock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import org.asdfgamer.sunriseClock.Settings.ID;

import static org.asdfgamer.sunriseClock.Settings.ID.apiKey;
import static org.asdfgamer.sunriseClock.Settings.ID.id;
import static org.asdfgamer.sunriseClock.Settings.ID.url;

public class MainActivity extends AppCompatActivity {

    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.settings= new Settings(getApplicationContext());
        loadSettings();
    }

    private void loadSettings() {
        String ip =settings.loadString(ID.ip,"");
        String api =settings.loadString(apiKey,"");
        String restID =settings.loadString(id,"");
        String complete =settings.loadString(url,"");
        setTextToEdit(R.id.ip_Text,ip);
        setTextToEdit(R.id.api_Text,api);
        setTextToEdit(R.id.id_Text,restID);
        setTextToEdit(R.id.complete_Text,complete);

    }

    public void saveSettings(View view)
    {
        String ip = getTextFromEdit(R.id.ip_Text);
        settings.save(ID.ip,ip);
        String api = getTextFromEdit(R.id.api_Text);
        settings.save(apiKey,api);
        String restID = getTextFromEdit(R.id.id_Text);
        settings.save(id,restID);
        String complete = getTextFromEdit(R.id.complete_Text);
        settings.save(url,complete);
    }

    public String getTextFromEdit(@IdRes int id)
    {
        EditText text = findViewById(id);
        return text.getText().toString();
    }

    public void setTextToEdit(@IdRes int id, String text)
    {
        EditText ip_text = findViewById(id);
        ip_text.setText(text);
    }

    public void syncAlarm(View view)
    {
        Intent startIntent = new Intent("AlarmReact");
        PendingIntent startPIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, startIntent, 0);
        AlarmManager alarm = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        //alarm.set(AlarmManager.RTC_WAKEUP, triggerTime, startPIntent);
    }
}
