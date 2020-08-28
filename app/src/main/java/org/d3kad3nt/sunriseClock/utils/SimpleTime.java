package org.d3kad3nt.sunriseClock.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SimpleTime {

    private Calendar date = new GregorianCalendar();

    public SimpleTime() {
    }

    public SimpleTime(long unixTime) {
        date.setTime(new Date(unixTime));
    }

    public SimpleTime(int hour, int minute) {
        date.set(Calendar.HOUR, hour);
        date.set(Calendar.MINUTE, minute);
    }

    public SimpleTime setSeconds(int seconds) {
        date.set(Calendar.SECOND, seconds);
        return this;
    }

    public SimpleTime setMinutes(int minutes) {
        date.set(Calendar.MINUTE, minutes);
        return this;
    }

    public SimpleTime setHours(int hours) {
        date.set(Calendar.HOUR, hours);
        return this;
    }

    public SimpleTime setDayOfMonth(int dayOfMonth) {
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return this;
    }

    public SimpleTime setMonth(int Month) {
        date.set(Calendar.MONTH, Month);
        return this;
    }

    public SimpleTime setYear(int year) {
        date.set(Calendar.YEAR, year);
        return this;
    }

    public long getUnixTime() {
        return date.getTimeInMillis();
    }

    public Calendar getCalender() {
        return date;
    }

    public Date getDate() {
        return date.getTime();
    }

    public String getTimeOfDay() {
        return date.get(Calendar.HOUR) + ":" + date.get(Calendar.MINUTE);
    }

}
