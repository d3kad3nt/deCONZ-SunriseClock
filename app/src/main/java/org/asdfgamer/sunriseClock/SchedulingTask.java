package org.asdfgamer.sunriseClock;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.asdfgamer.sunriseClock.network.DeconzConnection;
import org.asdfgamer.sunriseClock.utils.ISO8601;

import java.lang.ref.WeakReference;
import java.util.Date;

public class SchedulingTask extends AsyncTask<Void, Void, String> {

    private final String TAG = "SchedulingTask";

    private final BroadcastReceiver.PendingResult pendingResult;
    private final AlarmManager alarm;

    private WeakReference<Context> context;
    private Settings settings;

    SchedulingTask(BroadcastReceiver.PendingResult pendingResult, AlarmManager alarm, Context context) {
        this.pendingResult = pendingResult;
        this.alarm = alarm;

        this.context = new WeakReference<>(context);
        this.settings = new Settings(context);
    }

    @Override
    protected String doInBackground(Void... voids) {
        Date date = new Date();
        date.setTime(alarm.getNextAlarmClock().getTriggerTime());
        ISO8601 schedulingTime = new ISO8601(date);
        Log.i(TAG, "Next alarm rings at :" + schedulingTime);

        //TODO: Check if deconz url, apikey etc are valid. A 'valid' flag could be set when first adding or modifying these settings.
        //TODO: Improve defaultValues
        Uri baseUrl = Uri.parse(settings.loadString(Settings.ID.url, ""));
        String apiKey = settings.loadString(Settings.ID.apiKey, "");
        String lightId = settings.loadString(Settings.ID.id, "");

        DeconzConnection deconz = new DeconzConnection(baseUrl, apiKey);
        deconz.scheduleLight(lightId, schedulingTime);

        return "TODO" + schedulingTime;
    }

    @Override
    protected void onPostExecute(String s) {

        Toast.makeText(this.context.get(), s,
                Toast.LENGTH_LONG).show();

        // Must call finish() so the BroadcastReceiver can be recycled.
        pendingResult.finish();
    }

}

