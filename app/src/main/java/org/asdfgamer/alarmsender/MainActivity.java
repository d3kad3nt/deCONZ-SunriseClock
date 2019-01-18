package org.asdfgamer.alarmsender;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
        String ip =settings.loadString("ip","");
        String topic =settings.loadString("topic","");
        String api =settings.loadString("api","");
        String complete =settings.loadString("complete","");
        setTextToEdit(R.id.ip_Text,ip);
        setTextToEdit(R.id.topic_Text,topic);
        setTextToEdit(R.id.api_Text,api);
        setTextToEdit(R.id.complete_Text,complete);

    }

    public void saveSettings(View view)
    {
        String ip = getTextFromEdit(R.id.ip_Text);
        settings.save("ip",ip);
        String topic = getTextFromEdit(R.id.topic_Text);
        settings.save("topic",topic);
        String api = getTextFromEdit(R.id.api_Text);
        settings.save("api",api);
        String complete = getTextFromEdit(R.id.complete_Text);
        settings.save("complete",complete);
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
