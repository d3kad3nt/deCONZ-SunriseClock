package org.asdfgamer.alarmsender;

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

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarm == null || alarm.getNextAlarmClock() == null) {
            return;
        }
        long triggerTime = alarm.getNextAlarmClock().getTriggerTime();
        Log.i("AlarmSender", "Next alarm rings at :" + triggerTime);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = getURL(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("AlarmSender", "Published new alarm sucessfully");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w("AlarmSender", "Problem while publishing alarm");
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private String getURL(Context context) {
        Settings settings = new Settings(context);
        String url = settings.loadString("complete", "");
        if (url.isEmpty()) {
            String ip = settings.loadString("ip", "127.0.0.1");
            String topic = settings.loadString("topic", "/");
            String api = settings.loadString("api", "key");
            url = "http://" + ip + topic + api;
            Log.w("AlarmSender", "creating of URLS isn't implemented yet");
        }
        return url;
    }
}
