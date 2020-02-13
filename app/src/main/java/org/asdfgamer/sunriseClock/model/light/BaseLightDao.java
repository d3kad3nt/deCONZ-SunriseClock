package org.asdfgamer.sunriseClock.model.light;

import android.widget.ImageView;

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
public interface BaseLightDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = BaseLight.class)
    void save(BaseLight obj);

    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = BaseLight.class)
    long insertWithoutReplace(BaseLight obj);

    @Update(entity = BaseLight.class)
    void update(BaseLight obj);

    @Delete(entity = BaseLight.class)
    void delete(BaseLight obj);

    @Query("SELECT * FROM  'light_base' WHERE id = :id")
    LiveData<BaseLight> load(int id);

    @Transaction
    default void insertOrUpdate(BaseLight obj) {
        long id = insertWithoutReplace(obj);
        if (id == -1L) {
            update(obj);
        }
    }


//    @Query("SELECT * FROM " + "'" + BaseLight.TABLENAME + "'")
    @Query("SELECT * FROM 'light_base'")
    LiveData<List<BaseLight>> loadAllWithCaps();

    @Query("SELECT * FROM light_base WHERE cap_switchable = :switchable AND cap_dimmable = :dimmable AND cap_temperaturable = :temperaturable AND cap_colorable = :colorable")
    LiveData<List<BaseLight>> loadWithCap(boolean switchable, boolean dimmable, boolean temperaturable, boolean colorable);

//    @Query("SELECT * FROM light_base WHERE " + T.FILTER)
//    <T extends ICapability> LiveData<List<T>> loadWithCap(T capability);


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
