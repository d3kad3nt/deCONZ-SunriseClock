package org.asdfgamer.sunriseClock.model.endpoint;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface EndpointConfigDao {

    @Query("SELECT * FROM  'endpoint' WHERE endpointID = :id")
    LiveData<EndpointConfig> load(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = EndpointConfig.class)
    void save(EndpointConfig obj);

    @Transaction
    @Query("SELECT * FROM endpoint")
    LiveData<List<EndpointWithLights>> getEndpointWithLights();
}
