package org.d3kad3nt.sunriseClock.backend.data.local;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Transaction;
import java.util.List;
import java.util.Map;
import org.d3kad3nt.sunriseClock.backend.data.model.group.DbGroup;
import org.d3kad3nt.sunriseClock.backend.data.model.light.DbLight;

@Dao
public interface DbGroupDao extends DbEndpointEntityDao<DbGroup> {

    @Transaction
    default long getIdForEndpointIdAndEndpointEntityId(long endpointId, String endpointEntityId) {
        return getIdForEndpointIdAndEndpointGroupId(endpointId, endpointEntityId);
    }

    @Transaction
    default int updateUsingEndpointIdAndEndpointEntityId(@NonNull DbGroup group) {
        return updateUsingEndpointIdAndEndpointGroupId(
                group.getEndpointId(), group.getEndpointEntityId(), group.getName());
    }

    @Query(
            value = "SELECT id FROM `" + DbGroup.TABLENAME
                    + "` WHERE endpoint_id = :endpointId AND id_on_endpoint ="
                    + " :endpointGroupId")
    long getIdForEndpointIdAndEndpointGroupId(long endpointId, String endpointGroupId);

    @Query("UPDATE `" + DbGroup.TABLENAME + "` SET name = :name WHERE endpoint_id = "
            + ":endpointId AND id_on_endpoint = :endpointGroupId")
    int updateUsingEndpointIdAndEndpointGroupId(long endpointId, String endpointGroupId, String name);

    @Query("SELECT * FROM `" + DbGroup.TABLENAME + "` WHERE id = :groupId")
    LiveData<DbGroup> load(long groupId);

    @Query(value = "SELECT * FROM `" + DbGroup.TABLENAME + "` WHERE endpoint_id = :endpointId")
    LiveData<List<DbGroup>> loadAllForEndpoint(long endpointId);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query(
            value = "SELECT * FROM `group` "
                    + "INNER JOIN light_grouping ON light_grouping.group_id = `group`.id "
                    + "INNER JOIN light ON light.id = light_grouping.light_id "
                    + "WHERE `group`.endpoint_id = :endpointId")
    LiveData<Map<DbGroup, List<DbLight>>> loadGroupsWithLightsForEndpoint(long endpointId);
}
