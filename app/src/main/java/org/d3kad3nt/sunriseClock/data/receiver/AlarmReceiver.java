package org.d3kad3nt.sunriseClock.data.receiver;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.d3kad3nt.sunriseClock.R;

import java.util.Objects;

public class AlarmReceiver extends BroadcastReceiver {

    private final String TAG = "AlarmReceiver";

    private Context context;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.i(TAG, "Received intent.");

        this.context = context;

        if (!Objects.equals(intent.getAction(), "android.app.action.NEXT_ALARM_CLOCK_CHANGED")) {
            Log.w(context.getString(R.string.app_name),
                    "The received Broadcast had the wrong action: " + intent.getAction());
            return;
        }
        AlarmManager alarm = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        if (alarm == null || alarm.getNextAlarmClock() == null) {
            return;
        }

        removeObsoleteSchedules();
        if (checkPrerequisites()) {
            addSchedule(alarm, context);
        }

    }

    /* TODO: Retrieve formerly used schedule ids (probably from local storage) to remove obsolete schedulues from
        deconz.
     * Could use the new WorkManager from jetpack. */
    private void removeObsoleteSchedules() {

    }

    private void addSchedule(AlarmManager alarm, Context context) {
        final PendingResult pendingResult = goAsync();
        SchedulingTask asyncTask = new SchedulingTask(pendingResult, alarm, context);
        asyncTask.execute();
    }

    /**
     * Checks whether WiFi is enabled and device is connected to to a specific BSSID (from settings).
     *
     * @return True for device states matching the prerequisites.
     */
    private boolean checkPrerequisites() {
        WifiManager wifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String bssid = wifiInfo.getBSSID();
            Log.d(TAG, "Currently connected to BSSID: " + bssid);
            //TODO: Only execute if in defined wifi connection (TODO: settings element).
            return true;
        }
        else {
            Log.d(TAG, "WiFi is not enabled.");
            return false;
        }

    }

}
