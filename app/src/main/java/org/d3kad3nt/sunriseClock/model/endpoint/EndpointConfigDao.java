package org.d3kad3nt.sunriseClock.model.endpoint;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EndpointConfigDao {

    @Query("SELECT * FROM " + EndpointConfig.TABLENAME + " WHERE endpointId = :id")
    EndpointConfig load(long id);

    @Query("SELECT * FROM " + EndpointConfig.TABLENAME + " WHERE endpointId = :id")
    LiveData<EndpointConfig> loadLiveData(long id);

    @Query("SELECT * FROM " + EndpointConfig.TABLENAME)
    LiveData<List<EndpointConfig>> loadAll();

    @Insert(entity = EndpointConfig.class)
    long save(EndpointConfig obj);

    @Delete(entity = EndpointConfig.class)
    void delete(EndpointConfig obj);

    //@Transaction
    //@Query("SELECT * FROM " + EndpointConfig.TABLENAME)
    //LiveData<List<EndpointWithLights>> getEndpointWithLights();
}
