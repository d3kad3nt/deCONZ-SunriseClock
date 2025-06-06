package org.d3kad3nt.sunriseClock.backend.data.local.typeconverter;

import androidx.room.TypeConverter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonTypeConverter {

    @TypeConverter
    public String fromJsonObject(JsonObject value) {
        return value.toString();
    }

    @TypeConverter
    public JsonObject stringToJsonObject(String string) {
        return JsonParser.parseString(string).getAsJsonObject();
    }
}
