package org.d3kad3nt.sunriseClock.backend.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import org.d3kad3nt.sunriseClock.backend.data.model.endpoint.EndpointConfig;

@Dao
public interface EndpointConfigDao {

    @Query("SELECT * FROM " + EndpointConfig.TABLENAME + " WHERE endpointId = :id")
    LiveData<EndpointConfig> load(long id);

    @Query("SELECT * FROM " + EndpointConfig.TABLENAME)
    LiveData<List<EndpointConfig>> loadAll();

    /** @noinspection UnusedReturnValue*/
    @Insert(entity = EndpointConfig.class)
    long save(EndpointConfig obj);

    @Update(entity = EndpointConfig.class)
    void updateName(NameUpdate obj);

    @Delete(entity = EndpointConfig.class)
    void delete(EndpointConfig obj);

    class NameUpdate {

        @ColumnInfo(name = "endpointId")
        public long endpointId;

        @ColumnInfo(name = "name")
        public String name;
    }
}
