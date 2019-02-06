package org.asdfgamer.sunriseClock;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Objects;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (!Objects.equals(intent.getAction(), "android.app.action.NEXT_ALARM_CLOCK_CHANGED"))
        {
            Log.w(context.getString(R.string.app_name),"The received Broadcast had the wrong action: " + intent.getAction());
            return;
        }
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarm == null || alarm.getNextAlarmClock() == null) {
            return;
        }

        removeObsoleteSchedules();
        addSchedule(alarm, context);
    }

    /* TODO: Retrieve formerly used schedule ids (probably from local storage) to remove obsolete schedulues from deconz.
    * Could use the new WorkManager from jetpack. */
    private void removeObsoleteSchedules() {

    }

    private void addSchedule(AlarmManager alarm, Context context) {
        //TODO: Only execute if in defined wifi connection (TODO: settings element).
        final PendingResult pendingResult = goAsync();
        SchedulingTask asyncTask = new SchedulingTask(pendingResult, alarm, context);
        asyncTask.execute();
    }

}
