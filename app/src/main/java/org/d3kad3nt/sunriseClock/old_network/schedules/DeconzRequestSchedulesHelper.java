package org.d3kad3nt.sunriseClock.old_network.schedules;

import android.net.Uri;

import org.d3kad3nt.sunriseClock.old_network.schedules.model.Schedule;
import org.d3kad3nt.sunriseClock.old_network.schedules.model.ScheduleCommand;
import org.d3kad3nt.sunriseClock.old_network.schedules.model.ScheduleCommandBody;
import org.d3kad3nt.sunriseClock.old_network.utils.response.custDeserializer.model.Success;
import org.d3kad3nt.sunriseClock.old_network.utils.response.genericCallback.SimplifiedCallback;
import org.d3kad3nt.sunriseClock.util.ISO8601;

/**
 * Schedules provide the ability to trigger timed commands to groups or lights.
 *
 * Implements some useful endpoint-specific helper methods for requests to deconz.
 */
public class DeconzRequestSchedulesHelper extends DeconzRequestSchedules {
    public DeconzRequestSchedulesHelper(Uri baseUrl, String apiKey) {
        super(baseUrl, apiKey);
    }


    public void schedulePowerOn(CreateScheduleCallback callback, String lightId, ISO8601 date) {
        Schedule schedule = new Schedule();
        schedule.setCommand(new ScheduleCommand());
        schedule.getCommand().setScheduleCommandBody(new ScheduleCommandBody());

        schedule.setName("000SunriseClock");
        schedule.setDescription("created by SunriseClock for Deconz/Hue");
        schedule.setTime(date.toString());

        schedule.getCommand().setMethod("PUT");
        schedule.getCommand().setAddress(super.getFullApiUrl().buildUpon().appendPath("lights").appendPath(lightId).appendPath("state").build().getPath());

        schedule.getCommand().getBody().setOn(true);

        super.createSchedule(callback, schedule);
    }

    //TODO: Remove code repetitions for different callbacks
    public void schedulePowerOn(SimplifiedCallback<Success> callback, String lightId, ISO8601 date) {
        Schedule schedule = new Schedule();
        schedule.setCommand(new ScheduleCommand());
        schedule.getCommand().setScheduleCommandBody(new ScheduleCommandBody());

        schedule.setName("000SunriseClock");
        schedule.setDescription("created by SunriseClock for Deconz/Hue");
        schedule.setTime(date.toString());

        schedule.getCommand().setMethod("PUT");
        schedule.getCommand().setAddress(super.getFullApiUrl().buildUpon().appendPath("lights").appendPath(lightId).appendPath("state").build().getPath());

        schedule.getCommand().getBody().setOn(true);

        super.createSchedule(callback, schedule);
    }

}
