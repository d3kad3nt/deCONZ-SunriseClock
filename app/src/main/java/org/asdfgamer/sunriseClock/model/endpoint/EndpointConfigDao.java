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

    @Query("SELECT * FROM " + EndpointConfig.TABLENAME + " WHERE endpointId = :id")
    EndpointConfig load(long id);

    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = EndpointConfig.class)
    void save(EndpointConfig obj);

    //@Transaction
    //@Query("SELECT * FROM " + EndpointConfig.TABLENAME)
    //LiveData<List<EndpointWithLights>> getEndpointWithLights();
}
