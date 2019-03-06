package org.asdfgamer.sunriseClock;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.asdfgamer.sunriseClock.network.schedules.CreateScheduleCallback;
import org.asdfgamer.sunriseClock.network.schedules.DeconzRequestSchedulesHelper;
import org.asdfgamer.sunriseClock.network.utils.response.custDeserializer.model.Error;
import org.asdfgamer.sunriseClock.network.utils.response.custDeserializer.model.Success;
import org.asdfgamer.sunriseClock.network.utils.response.genericCallback.SimplifiedCallback;
import org.asdfgamer.sunriseClock.utils.ISO8601;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.Objects;

import androidx.preference.PreferenceManager;
import retrofit2.Call;
import retrofit2.Response;

public class SchedulingTask extends AsyncTask<Void, Void, String> {

    private final String TAG = "SchedulingTask";

    private final BroadcastReceiver.PendingResult pendingResult;
    private final AlarmManager alarm;

    private WeakReference<Context> context;
    private SharedPreferences preferences;

    SchedulingTask(BroadcastReceiver.PendingResult pendingResult, AlarmManager alarm, Context context) {
        this.pendingResult = pendingResult;
        this.alarm = alarm;

        this.context = new WeakReference<>(context);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this.context.get());
    }

    @Override
    protected String doInBackground(Void... voids) {
        Date date = new Date();
        date.setTime(alarm.getNextAlarmClock().getTriggerTime());
        ISO8601 schedulingTime = new ISO8601(date);
        Log.i(TAG, "Next alarm rings at :" + schedulingTime);

        //TODO: Check if deconz url, apikey etc are valid. A 'valid' flag could be set when first adding or modifying these settings.
        //TODO: Improve defaultValues
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(preferences.getString("pref_ip", "") + ":" + preferences.getString("pref_port", ""));
        String apiKey = preferences.getString("pref_api_key", "");
        String lightId = preferences.getString("", "2"); //TODO: setting options for light ids

        DeconzRequestSchedulesHelper deconz = new DeconzRequestSchedulesHelper(builder.build(), apiKey);
        deconz.schedulePowerOn(new SimplifiedCallback<Success>() {
            @Override
            public void onSuccess(Response<Success> response) {
                Success success = response.body();
                Log.i(TAG, "Successfully created schedule with id: " + Objects.requireNonNull(success).getId());
            }

            @Override
            public void onError() {
                //TODO
            }
        }, lightId, schedulingTime);

        return "TODO" + schedulingTime;
    }

    @Override
    protected void onPostExecute(String s) {

        if (preferences.getBoolean("pref_schedule_set_toast", true)) {
            Toast.makeText(this.context.get(), s,
                    Toast.LENGTH_LONG).show();
        }

        // Must call finish() so the BroadcastReceiver can be recycled.
        pendingResult.finish();
    }

}

