package org.d3kad3nt.sunriseClock.backend.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface DbLightGroupingDao {

    /** @noinspection unused*/
    @Query("SELECT * FROM " + DbGroupLightCrossref.TABLENAME)
    LiveData<List<DbGroupLightCrossref>> loadAll();

    /** @noinspection UnusedReturnValue*/
    @Insert(entity = DbGroupLightCrossref.class, onConflict = OnConflictStrategy.REPLACE)
    long save(DbGroupLightCrossref obj);

    /** @noinspection unused*/
    @Delete(entity = DbGroupLightCrossref.class)
    void delete(DbGroupLightCrossref obj);
}
