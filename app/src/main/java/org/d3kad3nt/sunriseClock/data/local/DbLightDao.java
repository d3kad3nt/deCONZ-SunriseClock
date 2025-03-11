package org.d3kad3nt.sunriseClock.data.local;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import org.d3kad3nt.sunriseClock.data.model.light.DbLight;

import java.util.List;

@Dao
public interface DbLightDao extends DbEndpointEntityDao<DbLight> {

    @Transaction
    default int updateUsingEndpointIdAndEndpointEntityId(@NonNull DbLight light) {
        return updateUsingEndpointIdAndEndpointLightId(light.getEndpointId(), light.getEndpointEntityId(),
            light.getName(), light.getIsSwitchable(), light.getIsOn(), light.getIsDimmable(), light.getBrightness(),
            light.getIsTemperaturable(), light.getColorTemperature(), light.getIsColorable(), light.getColor());
    }

    @Query("UPDATE " + DbLight.TABLENAME +
        " SET name = :name, is_switchable = :switchable, is_on = :on, is_dimmable  = :dimmable, brightness = " +
        ":brightness, is_temperaturable = :temperaturable, " +
        "colortemperature = :colorTemperature, is_colorable = :colorable, color = :color WHERE endpoint_id = " +
        ":endpointId AND id_on_endpoint = :endpointLightId")
    int updateUsingEndpointIdAndEndpointLightId(long endpointId, String endpointLightId, String name,
                                                boolean switchable, boolean on, boolean dimmable, int brightness,
                                                boolean temperaturable, int colorTemperature, boolean colorable,
                                                int color);

    @Query("SELECT * FROM " + DbLight.TABLENAME + " WHERE id = :lightId")
    LiveData<DbLight> load(long lightId);

    @Query("SELECT * FROM " + DbLight.TABLENAME + " WHERE endpoint_id = :endpointId")
    LiveData<List<DbLight>> loadAllForEndpoint(long endpointId);
}
