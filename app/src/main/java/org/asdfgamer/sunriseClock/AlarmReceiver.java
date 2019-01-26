package org.asdfgamer.sunriseClock;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Objects;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (!Objects.equals(intent.getAction(), "android.app.action.NEXT_ALARM_CLOCK_CHANGED"))
        {
            Log.w(context.getString(R.string.app_name),"The received Broadcast had the wrong action: " + intent.getAction());
            return;
        };
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarm == null || alarm.getNextAlarmClock() == null) {
            return;
        }
        long triggerTime = alarm.getNextAlarmClock().getTriggerTime();
        Log.i(context.getString(R.string.app_name), "Next alarm rings at :" + triggerTime);


    }
}
