package org.d3kad3nt.sunriseClock.network.schedules.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduleCommandBody {
    @SerializedName("on")
    @Expose
    private Boolean on;

    public Boolean getOn() {
        return on;
    }

    public void setOn(Boolean on) {
        this.on = on;
    }
}
