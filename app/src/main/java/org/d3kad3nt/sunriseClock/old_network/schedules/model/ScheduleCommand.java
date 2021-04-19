package org.d3kad3nt.sunriseClock.old_network.schedules.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduleCommand {
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("body")
    @Expose
    private ScheduleCommandBody scheduleCommandBody;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public ScheduleCommandBody getBody() {
        return scheduleCommandBody;
    }

    public void setScheduleCommandBody(ScheduleCommandBody body) {
        this.scheduleCommandBody = body;
    }
}
