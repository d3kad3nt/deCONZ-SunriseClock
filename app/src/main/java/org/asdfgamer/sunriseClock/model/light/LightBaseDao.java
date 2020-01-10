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
public interface LightBaseDao<T extends LightBase> {

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = LightBase.class)
    void save(T obj);

    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = LightBase.class)
    long insertWithoutReplace(T obj);

    @Update(entity = LightBase.class)
    void update(T obj);

    @Delete(entity = LightBase.class)
    void delete(T obj);

    @Transaction
    default void insertOrUpdate(T obj) {
        long id = insertWithoutReplace(obj);
        if (id == -1L) {
            update(obj);
        }
    }

    //@Query("SELECT * FROM " + "'" + T.TABLENAME + "'")
    //@Query("SELECT * FROM 'light_base'")
    //LiveData<List<T>> loadAllWithCaps();

    @Query("SELECT * FROM 'light_base' WHERE cap_switchable = :switchable AND cap_dimmable = :dimmable AND cap_temperaturable = :temperaturable AND cap_colorable = :colorable")
    LiveData<List<T>> loadWithCap(boolean switchable, boolean dimmable, boolean temperaturable, boolean colorable);

    //@Query("SELECT * FROM 'light_base' WHERE cap_switchable = :switchable AND cap_dimmable = :dimmable AND cap_temperaturable = :temperaturable AND cap_colorable = :colorable")
    //LiveData<List<T>> loadAllWithCaps(boolean switchable, boolean dimmable, boolean temperaturable, boolean colorable);

    //@Query("SELECT * FROM " + T.)
    //LiveData<List<T>> loadAll();

    //default LiveData<List<LightRemote_SwitchableUndimmableUntemperaturableUncolorable>> loadLightsFor(LightRemote_SwitchableUndimmableUntemperaturableUncolorable obj) {
    //    return loadAllWithCaps(obj.switchable, obj.dimmable, obj.temperaturable, obj.colorable);
    //}




    //default LiveData<List<LightRemote_SwitchableUndimmableUntemperaturableUncolorable>> getForClass(Class<LightRemote_SwitchableUndimmableUntemperaturableUncolorable> clazz) {
    //    return loadAllWithCaps(clazz.getDeclaredField("SWITCHABLE"));
    //}

}
