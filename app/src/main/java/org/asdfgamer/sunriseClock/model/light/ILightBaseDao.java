package org.asdfgamer.sunriseClock.model.light;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ILightBaseDao<T extends LightBase> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(T obj);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertWithoutReplace(T obj);

    @Update
    void update(T obj);

    @Delete
    void delete(T obj);

    @Transaction
    default void insertOrUpdate(T obj) {
        long id = insertWithoutReplace(obj);
        if (id == -1L) {
            update(obj);
        }
    }

    @Query("SELECT * FROM " + "'" + T.TABLENAME + "'")
    LiveData<List<T>> loadAll();
}
