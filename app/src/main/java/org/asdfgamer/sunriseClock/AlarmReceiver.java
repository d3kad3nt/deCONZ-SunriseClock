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
        if (!Objects.equals(intent.getAction(), ""))
        {
            Log.w(context.getString(R.string.app_name),"The recieved Broadcast had the wrong action: " + intent.getAction());
            return;
        };
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarm == null || alarm.getNextAlarmClock() == null) {
            return;
        }
        long triggerTime = alarm.getNextAlarmClock().getTriggerTime();
        Log.i(context.getString(R.string.app_name), "Next alarm rings at :" + triggerTime);

        // Instantiate the RequestQueue.
        Settings settings = new Settings(context);
        String url = settings.loadString(Settings.ID.url, "");
        if (url.isEmpty())
        {
            Log.w(context.getString(R.string.app_name),"No URL given!");
            return;
        }
        // Request a string response from the provided URL.
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(context.getString(R.string.app_name), "Published new alarm sucessfully");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w(context.getString(R.string.app_name), "Problem while publishing alarm");
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}
