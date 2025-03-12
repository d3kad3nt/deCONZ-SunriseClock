package org.d3kad3nt.sunriseClock.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import org.d3kad3nt.sunriseClock.data.model.group.DbGroup;
import org.d3kad3nt.sunriseClock.data.model.groupWithLights.DbGroupLightCrossref;
import org.d3kad3nt.sunriseClock.data.model.groupWithLights.DbGroupWithLights;

import java.util.List;

@Dao
public interface DbGroupLightCrossrefDao {

    @Transaction
    @Query("SELECT * FROM '" + DbGroup.TABLENAME + "'")
    LiveData<List<DbGroupWithLights>> loadGroupsWithLights();

    @Query("SELECT * FROM " + DbGroupLightCrossref.TABLENAME)
    LiveData<List<DbGroupLightCrossref>> loadAll();

    @Insert(entity = DbGroupLightCrossref.class)
    long save(DbGroupLightCrossref obj);

    @Delete(entity = DbGroupLightCrossref.class)
    void delete(DbGroupLightCrossref obj);
}