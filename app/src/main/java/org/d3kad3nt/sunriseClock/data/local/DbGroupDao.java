package org.d3kad3nt.sunriseClock.data.local;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import org.d3kad3nt.sunriseClock.data.model.group.DbGroup;

import java.util.List;

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
}
