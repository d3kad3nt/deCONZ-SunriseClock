package org.d3kad3nt.sunriseClock.network.schedules.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Schedule {
    @SerializedName("autodelete")
    @Expose
    private Boolean autodelete;
    @SerializedName("command")
    @Expose
    private ScheduleCommand scheduleCommand;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("etag")
    @Expose
    private String etag;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("time")
    @Expose
    private String time;

    public Boolean getAutodelete() {
        return autodelete;
    }

    public void setAutodelete(Boolean autodelete) {
        this.autodelete = autodelete;
    }

    public ScheduleCommand getCommand() {
        return scheduleCommand;
    }

    public void setCommand(ScheduleCommand command) {
        this.scheduleCommand = command;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
