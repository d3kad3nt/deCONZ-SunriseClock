package org.asdfgamer.sunriseClock.model.light;

import android.util.Log;

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

    @Query("SELECT * FROM  'light' WHERE lightID = :id")
    LiveData<BaseLight> load(int id);

    @Transaction
    default void insertOrUpdate(BaseLight obj) {
        long id = insertWithoutReplace(obj);
        if (id == -1L) {
            update(obj);
        }
    }

    @Query("SELECT * FROM light WHERE cap_switchable = :switchable AND cap_dimmable = :dimmable AND cap_temperaturable = :temperaturable AND cap_colorable = :colorable")
    LiveData<List<BaseLight>> loadWithCap(boolean switchable, boolean dimmable, boolean temperaturable, boolean colorable);

    @Query("SELECT * FROM light WHERE cap_switchable = :switchable AND cap_dimmable = :dimmable AND cap_temperaturable = :temperaturable ")
    LiveData<List<BaseLight>> loadWithCapSwitchDimmTemp(boolean switchable, boolean dimmable, boolean temperaturable);

    @Query("SELECT * FROM light WHERE cap_switchable = :switchable AND cap_dimmable = :dimmable AND cap_colorable = :colorable")
    LiveData<List<BaseLight>> loadWithCapSwitchDimmColor(boolean switchable, boolean dimmable,  boolean colorable);

    @Query("SELECT * FROM light WHERE cap_switchable = :switchable  AND cap_temperaturable = :temperaturable AND cap_colorable = :colorable")
    LiveData<List<BaseLight>> loadWithCapSwitchTempColor(boolean switchable, boolean temperaturable, boolean colorable);

    @Query("SELECT * FROM light WHERE cap_switchable = :switchable AND cap_dimmable = :dimmable")
    LiveData<List<BaseLight>> loadWithCapSwitchDimm(boolean switchable, boolean dimmable);

    @Query("SELECT * FROM light WHERE cap_switchable = :switchable AND cap_temperaturable = :temperaturable")
    LiveData<List<BaseLight>> loadWithCapSwitchTemp(boolean switchable, boolean temperaturable);

    @Query("SELECT * FROM light WHERE cap_switchable = :switchable AND cap_colorable = :colorable")
    LiveData<List<BaseLight>> loadWithCapSwitchColor(boolean switchable, boolean colorable);

    @Query("SELECT * FROM light WHERE cap_switchable = :switchable")
    LiveData<List<BaseLight>> loadWithCapSwitch(boolean switchable);

    @Query("SELECT * FROM light WHERE cap_dimmable = :dimmable AND cap_temperaturable = :temperaturable AND cap_colorable = :colorable")
    LiveData<List<BaseLight>> loadWithCapDimmTempColor(boolean dimmable, boolean temperaturable, boolean colorable);

    @Query("SELECT * FROM light WHERE cap_dimmable = :dimmable AND cap_colorable = :colorable")
    LiveData<List<BaseLight>> loadWithCapDimmColor(boolean dimmable,  boolean colorable);

    @Query("SELECT * FROM light WHERE cap_dimmable = :dimmable  AND cap_temperaturable = :temperaturable")
    LiveData<List<BaseLight>> loadWithCapDimmTemp(boolean dimmable, boolean temperaturable);

    @Query("SELECT * FROM light WHERE cap_dimmable = :dimmable ")
    LiveData<List<BaseLight>> loadWithCapDimm(boolean dimmable);

    @Query("SELECT * FROM light WHERE cap_temperaturable = :temperaturable AND cap_colorable = :colorable")
    LiveData<List<BaseLight>> loadWithCapTempColor(boolean temperaturable, boolean colorable);

    @Query("SELECT * FROM light WHERE cap_temperaturable = :temperaturable")
    LiveData<List<BaseLight>> loadWithCapTemp(boolean temperaturable);

    @Query("SELECT * FROM light WHERE  cap_colorable = :colorable")
    LiveData<List<BaseLight>> loadWithCapColor(boolean colorable);

    default LiveData<List<BaseLight>> loadWithCap(Class<? extends ICapability>... capabilities ) {
        boolean switchable = false;
        boolean dimmable = false;
        boolean temperaturable = false;
        boolean colorable = false;
        for (Class capability : capabilities){
            switch (capability.getSimpleName()){
                case "ILightRemoteColorable":colorable = true;
                        break;
                case "ILightRemoteDimmable": dimmable = true;
                break;
                case "ILightRemoteSwitchable": switchable = true;
                break;
                case "ILightRemoteTemperaturable": temperaturable = true;
                break;
                default:
                    Log.e("BaseLightDAO", "getLightByCapability: The Capability '" + capability.getSimpleName() + "' is unknown");
            }
        }
        if(switchable){
            if(dimmable){
                if(temperaturable){
                    if(colorable){
                        return loadWithCap(switchable,dimmable,temperaturable,colorable);
                    }else {
                        return loadWithCapSwitchDimmTemp(switchable, dimmable, temperaturable);
                    }
                }else {
                    if (colorable){
                        return loadWithCapSwitchDimmColor(switchable,dimmable,colorable);
                    }else{
                        return loadWithCapSwitchDimm(switchable,dimmable);
                    }
                }
            }else{
                if(temperaturable){
                    if(colorable){
                        return loadWithCapSwitchTempColor(switchable,temperaturable,colorable);
                    }else {
                        return loadWithCapSwitchTemp(switchable, temperaturable);
                    }
                }else {
                    if (colorable){
                        return loadWithCapSwitchColor(switchable,colorable);
                    }else{
                        return loadWithCapSwitch(switchable);
                    }
                }
            }
        }else{
            if(dimmable){
                if(temperaturable){
                    if(colorable){
                        return loadWithCapDimmTempColor(dimmable,temperaturable,colorable);
                    }else {
                        return loadWithCapDimmTemp(dimmable, temperaturable);
                    }
                }else {
                    if (colorable){
                        return loadWithCapDimmColor(dimmable,colorable);
                    }else{
                        return loadWithCapDimm(dimmable);
                    }
                }
            }else{
                if(temperaturable){
                    if(colorable){
                        return loadWithCapTempColor(temperaturable,colorable);
                    }else {
                        return loadWithCapTemp(temperaturable);
                    }
                }else {
                    if (colorable){
                        return loadWithCapColor(colorable);
                    }else{
                        return loadWithCap(false,false,false,false);
                    }
                }
            }
        }
    }

        @Query("SELECT * FROM 'light'")
    LiveData<List<BaseLight>> loadAll();

}
