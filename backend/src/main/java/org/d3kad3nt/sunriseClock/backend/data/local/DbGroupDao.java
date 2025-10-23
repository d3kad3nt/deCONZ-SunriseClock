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
                group.getEndpointId(),
                group.getEndpointEntityId(),
                group.getName(),
                group.getIsOnAny(),
                group.getIsOnAll());
    }

    @Query(
            value = "SELECT id FROM '" + DbGroup.TABLENAME
                    + "' WHERE endpoint_id = :endpointId AND id_on_endpoint ="
                    + " :endpointGroupId")
    long getIdForEndpointIdAndEndpointGroupId(long endpointId, String endpointGroupId);

    @Query("UPDATE '" + DbGroup.TABLENAME
            + "' SET name = :name, is_on_any = :isOnAny, is_on_all = :isOnAll WHERE endpoint_id = "
            + ":endpointId AND id_on_endpoint = :endpointGroupId")
    int updateUsingEndpointIdAndEndpointGroupId(
            long endpointId, String endpointGroupId, String name, boolean isOnAny, boolean isOnAll);

    @Query("SELECT * FROM '" + DbGroup.TABLENAME + "' WHERE id = :groupId")
    LiveData<DbGroup> load(long groupId);

    @Query(value = "SELECT * FROM '" + DbGroup.TABLENAME + "' WHERE endpoint_id = :endpointId")
    LiveData<List<DbGroup>> loadAllForEndpoint(long endpointId);

    // Normally we would annotate this method with @RewriteQueriesToDropUnusedColumns as some
    // columns do not need to
    // be returned. This prevents the following error message: 'The query returns some columns
    // [group_id, light_id]
    // which are not used by any of DbLight, DbGroup.'
    // However this is not possible as the documentation states: 'Note that Room will not rewrite
    // the query if it has
    // multiple columns that have the same name as it does not yet have a way to distinguish which
    // one is necessary.'
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query(
            value = "SELECT * FROM `group` "
                    + "INNER JOIN light_grouping ON light_grouping.group_id = `group`.id "
                    + "INNER JOIN light ON light.id = light_grouping.light_id "
                    + "WHERE `group`.endpoint_id = :endpointId")
    LiveData<Map<DbGroup, List<DbLight>>> loadGroupsWithLightsForEndpoint(long endpointId);
}
