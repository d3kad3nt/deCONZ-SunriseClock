package org.d3kad3nt.sunriseClock.data.local;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Transaction;

import org.d3kad3nt.sunriseClock.data.model.group.DbGroup;
import org.d3kad3nt.sunriseClock.data.model.light.DbLight;

import java.util.List;
import java.util.Map;

@Dao
public interface DbLightDao extends DbEndpointEntityDao<DbLight> {

    @Override
    default long getIdForEndpointIdAndEndpointEntityId(long endpointId, String endpointEntityId) {
        return getIdForEndpointIdAndEndpointLightId(endpointId, endpointEntityId);
    }

    @Query(value = "SELECT id FROM '" + DbLight.TABLENAME + "' WHERE endpoint_id = :endpointId AND id_on_endpoint =" +
        " :endpointLightId")
    long getIdForEndpointIdAndEndpointLightId(long endpointId, String endpointLightId);

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

    // Normally we would annotate this method with @RewriteQueriesToDropUnusedColumns as some columns do not need to
    // be returned. This prevents the following error message: 'The query returns some columns [group_id, light_id]
    // which are not used by any of DbLight, DbGroup.'
    // However this is not possible as the documentation states: 'Note that Room will not rewrite the query if it has
    // multiple columns that have the same name as it does not yet have a way to distinguish which one is necessary.'
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query(value = "SELECT * FROM light " +
        "INNER JOIN light_grouping ON light_grouping.light_id = light.id " +
        "INNER JOIN `group` ON `group`.id = light_grouping.group_id " +
        "WHERE light.endpoint_id = :endpointId")
    LiveData<Map<DbLight, List<DbGroup>>> loadLightsWithGroupsForEndpoint(long endpointId);
}
