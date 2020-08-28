package org.d3kad3nt.sunriseClock.model.endpoint;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.JsonObject;

import org.d3kad3nt.sunriseClock.model.endpoint.util.DateTypeConverter;
import org.d3kad3nt.sunriseClock.model.endpoint.util.EndpointTypeConverter;
import org.d3kad3nt.sunriseClock.model.endpoint.util.JsonTypeConverter;

import java.util.Date;

@Entity(tableName = EndpointConfig.TABLENAME)
public class EndpointConfig {

    static final String TABLENAME = "endpoint";

    @PrimaryKey
    @ColumnInfo(name = "endpointId")
    public int id;

    @ColumnInfo(name = "type")
    @TypeConverters(EndpointTypeConverter.class)
    public EndpointType type;

    //TODO: Maybe we have to use a separate library to handle timezones correctly.
    // Class LocalDateTime is timezone-sensitive, but is not supported until API level 26.
    @TypeConverters(DateTypeConverter.class)
    @ColumnInfo(name = "date_added")
    private Date addedAt;

    @TypeConverters(JsonTypeConverter.class)
    @ColumnInfo(name = "config")
    private JsonObject jsonConfig;

    public EndpointConfig(int id, EndpointType type, Date addedAt, JsonObject jsonConfig) {
        this.id = id;
        this.type = type;

        this.addedAt = addedAt;
        this.jsonConfig = jsonConfig;
    }

    public int getId() {
        return id;
    }

    public EndpointType getType() {
        return type;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public JsonObject getJsonConfig() {
        return jsonConfig;
    }
}
