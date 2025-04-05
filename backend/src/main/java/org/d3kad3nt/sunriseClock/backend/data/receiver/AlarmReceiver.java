package org.d3kad3nt.sunriseClock.backend.data.receiver;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import java.util.Objects;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class AlarmReceiver extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        LogUtil.i("Received intent.");

        this.context = context;

        if (!Objects.equals(intent.getAction(), "android.app.action.NEXT_ALARM_CLOCK_CHANGED")) {
            LogUtil.w("The received Broadcast had the wrong action: %s", intent.getAction());
            return;
        }
        AlarmManager alarm = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        if (alarm == null || alarm.getNextAlarmClock() == null) {
            return;
        }

        removeObsoleteSchedules();
        if (checkPrerequisites()) {
            // Add Schedule
        }
    }

    /**
     * TODO: Retrieve formerly used schedule ids (probably from local storage) to remove obsolete schedulues from
     * deconz. Could use the new WorkManager from jetpack.
     *
     * @noinspection EmptyMethod */
    private void removeObsoleteSchedules() {}

    /**
     * Checks whether WiFi is enabled and device is connected to to a specific BSSID (from settings).
     *
     * @return True for device states matching the prerequisites.
     */
    private boolean checkPrerequisites() {
        WifiManager wifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            return true;
        } else {
            LogUtil.d("WiFi is not enabled.");
            return false;
        }
    }
}
