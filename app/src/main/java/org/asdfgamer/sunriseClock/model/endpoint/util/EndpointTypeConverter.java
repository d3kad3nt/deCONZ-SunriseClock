package org.asdfgamer.sunriseClock.model.endpoint.util;

import androidx.room.TypeConverter;

import org.asdfgamer.sunriseClock.model.endpoint.EndpointType;

public class EndpointTypeConverter {

    @TypeConverter
    public static EndpointType toType(int type) {

        for (EndpointType t : EndpointType.values()) {
            if(t.getId() == type){
                return t;
            }
        }
        throw new IllegalArgumentException("No type in ENUM EndpointType for type id: " + type);
    }

    @TypeConverter
    public static Integer toInteger(EndpointType type) {
        return type.getId();
    }

}
