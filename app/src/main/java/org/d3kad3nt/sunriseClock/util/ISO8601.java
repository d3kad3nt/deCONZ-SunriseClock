package org.d3kad3nt.sunriseClock.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ISO8601 {

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

    private final String iso8601;
    private final Date date;

    /**
     * @param date Time in UTC (!)
     */
    public ISO8601(Date date) {
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.date = date;
        this.iso8601 = formatToIso(date);
    }

    /**
     * @param iso8601 iso8601 formatted time string with format yyyy-MM-dd'T'HH:mm:ss
     */
    public ISO8601(String iso8601) {
        try {
            this.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            this.date = getFromIso(iso8601);
            this.iso8601 = iso8601;
        }
        catch (java.text.ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * @param time Time to convert in UTC (!)
     * @return Properly formatted date string for deconz.
     */
    private String formatToIso(Date time) {
        return dateFormat.format(time.getTime());
    }

    /**
     * @param time Time to convert in UTC (!)
     * @return Date object in UTC timezone.
     */
    private Date getFromIso(String time) throws ParseException {
        return dateFormat.parse(time);
    }

    public Date getDate() {
        return this.date;
    }

    public String getIso8601() {
        return this.iso8601;
    }

    @Override
    public String toString() {
        return getIso8601();
    }
}
