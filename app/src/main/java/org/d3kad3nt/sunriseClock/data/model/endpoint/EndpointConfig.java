package org.d3kad3nt.sunriseClock.data.model.endpoint;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.google.gson.JsonObject;
import java.util.Date;
import java.util.Objects;
import org.d3kad3nt.sunriseClock.data.local.typeconverter.DateTypeConverter;
import org.d3kad3nt.sunriseClock.data.local.typeconverter.EndpointTypeConverter;
import org.d3kad3nt.sunriseClock.data.local.typeconverter.JsonTypeConverter;

@Entity(tableName = EndpointConfig.TABLENAME)
public class EndpointConfig {

  public static final String TABLENAME = "endpoint";

  // TODO: Maybe we have to use a separate library to handle timezones correctly.
  // Class LocalDateTime is timezone-sensitive, but is not supported until API level 26.
  @TypeConverters(DateTypeConverter.class)
  @ColumnInfo(name = "date_added")
  private final Date addedAt;

  @TypeConverters(JsonTypeConverter.class)
  @ColumnInfo(name = "config")
  private final JsonObject jsonConfig;

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "endpointId")
  public long id;

  @ColumnInfo(name = "type")
  @TypeConverters(EndpointTypeConverter.class)
  public EndpointType type;

  @SuppressWarnings("NotNullFieldNotInitialized")
  @ColumnInfo(name = "name", defaultValue = "Unnamed Endpoint")
  @NonNull
  private String name;

  public EndpointConfig(EndpointType type, String name, Date addedAt, JsonObject jsonConfig) {
    this.type = type;
    this.name = name;
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

  public final JsonObject getJsonConfig() {
    return jsonConfig;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EndpointConfig that = (EndpointConfig) o;
    return id == that.id;
  }

  @NonNull
  @Override
  public String toString() {
    return name;
  }
}
