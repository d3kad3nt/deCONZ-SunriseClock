package org.d3kad3nt.sunriseClock.model.endpoint;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.JsonObject;

import org.d3kad3nt.sunriseClock.model.endpoint.util.DateTypeConverter;
import org.d3kad3nt.sunriseClock.model.endpoint.util.EndpointTypeConverter;
import org.d3kad3nt.sunriseClock.model.endpoint.util.JsonTypeConverter;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = EndpointConfig.TABLENAME)
public class EndpointConfig {

    static final String TABLENAME = "endpoint";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "endpointId")
    public long id;

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

    public EndpointConfig(EndpointType type, Date addedAt, JsonObject jsonConfig) {
        this.type = type;

        this.addedAt = addedAt;
        this.jsonConfig = jsonConfig;
    }

    public long getId() {
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

    @NonNull
    @Override
    public String toString() {
        return Long.toString(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EndpointConfig that = (EndpointConfig) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
