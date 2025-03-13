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
public interface DbGroupDao extends DbEndpointEntityDao<DbGroup> {

    String TAG = "DbGroupDao";

    @Query("UPDATE '" + DbGroup.TABLENAME + "' SET name = :name WHERE endpoint_id = " +
        ":endpointId AND id_on_endpoint = :endpointGroupId")
    int updateUsingEndpointIdAndEndpointGroupId(long endpointId, String endpointGroupId, String name);

    @Transaction
    default int updateUsingEndpointIdAndEndpointEntityId(@NonNull DbGroup group) {
        return updateUsingEndpointIdAndEndpointGroupId(group.getEndpointId(), group.getEndpointEntityId(),
            group.getName());
    }

    @Query(value = "SELECT * FROM '" + DbGroup.TABLENAME + "' WHERE endpoint_id = :endpointId")
    LiveData<List<DbGroup>> loadAllForEndpoint(long endpointId);

    // Normally we would annotate this method with @RewriteQueriesToDropUnusedColumns as some columns do not need to
    // be returned. This prevents the following error message: 'The query returns some columns [group_id, light_id]
    // which are not used by any of DbLight, DbGroup.'
    // However this is not possible as the documentation states: 'Note that Room will not rewrite the query if it has
    // multiple columns that have the same name as it does not yet have a way to distinguish which one is necessary.'
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query(value = "SELECT * FROM `group` " +
        "INNER JOIN light_grouping ON light_grouping.group_id = `group`.id " +
        "INNER JOIN light ON light.id = light_grouping.light_id " +
        "WHERE `group`.endpoint_id = :endpointId")
    LiveData<Map<DbGroup, List<DbLight>>> loadGroupsWithLightsForEndpoint(long endpointId);
}
