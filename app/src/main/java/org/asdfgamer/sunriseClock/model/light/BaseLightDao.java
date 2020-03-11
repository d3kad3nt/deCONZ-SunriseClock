package org.asdfgamer.sunriseClock.model.light;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface BaseLightDao {

    String TAG = "BaseLightDao";

    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = BaseLight.class)
    long save(BaseLight obj);

    @Update(entity = BaseLight.class)
    void update(BaseLight obj);

    @Delete(entity = BaseLight.class)
    void delete(BaseLight obj);

    @Query("SELECT * FROM " + BaseLight.TABLENAME + " WHERE endpointId = :endpointId AND endpointLightId = :endpointLightId")
    LiveData<BaseLight> load(int endpointId, String endpointLightId);

    @Query("SELECT * FROM " + BaseLight.TABLENAME + " WHERE endpointId = :endpointId")
    LiveData<List<BaseLight>> loadAllForEndpoint(int endpointId);

    @Transaction
    default void upsert(BaseLight obj) {
        long id = save(obj);

        if (id == -1L) {
            update(obj);
            Log.d(TAG, "Updated BaseLight with endpointId " + obj.getEndpointId() + " and endpointLightId: " + obj.getEndpointLightId());
        }
        else {
            Log.d(TAG, "Inserted BaseLight with endpointId " + obj.getEndpointId() + " and endpointLightId: " + obj.getEndpointLightId());
        }
    }



    @Query("SELECT * FROM " + BaseLight.TABLENAME + " WHERE isSwitchable = :switchable AND isDimmable = :dimmable AND isTemperaturable = :temperaturable AND isColorable = :colorable")
    LiveData<List<BaseLight>> loadWithCap(boolean switchable, boolean dimmable, boolean temperaturable, boolean colorable);

    @Query("SELECT * FROM " + BaseLight.TABLENAME + " WHERE isSwitchable = :switchable AND isDimmable = :dimmable AND isTemperaturable = :temperaturable ")
    LiveData<List<BaseLight>> loadWithCapSwitchDimmTemp(boolean switchable, boolean dimmable, boolean temperaturable);

    @Query("SELECT * FROM " + BaseLight.TABLENAME + " WHERE isSwitchable = :switchable AND isDimmable = :dimmable AND isColorable = :colorable")
    LiveData<List<BaseLight>> loadWithCapSwitchDimmColor(boolean switchable, boolean dimmable,  boolean colorable);

    @Query("SELECT * FROM " + BaseLight.TABLENAME + " WHERE isSwitchable = :switchable  AND isTemperaturable = :temperaturable AND isColorable = :colorable")
    LiveData<List<BaseLight>> loadWithCapSwitchTempColor(boolean switchable, boolean temperaturable, boolean colorable);

    @Query("SELECT * FROM " + BaseLight.TABLENAME + " WHERE isSwitchable = :switchable AND isDimmable = :dimmable")
    LiveData<List<BaseLight>> loadWithCapSwitchDimm(boolean switchable, boolean dimmable);

    @Query("SELECT * FROM " + BaseLight.TABLENAME + " WHERE isSwitchable = :switchable AND isTemperaturable = :temperaturable")
    LiveData<List<BaseLight>> loadWithCapSwitchTemp(boolean switchable, boolean temperaturable);

    @Query("SELECT * FROM " + BaseLight.TABLENAME + " WHERE isSwitchable = :switchable AND isColorable = :colorable")
    LiveData<List<BaseLight>> loadWithCapSwitchColor(boolean switchable, boolean colorable);

    @Query("SELECT * FROM " + BaseLight.TABLENAME + " WHERE isSwitchable = :switchable")
    LiveData<List<BaseLight>> loadWithCapSwitch(boolean switchable);

    @Query("SELECT * FROM " + BaseLight.TABLENAME + " WHERE isDimmable = :dimmable AND isTemperaturable = :temperaturable AND isColorable = :colorable")
    LiveData<List<BaseLight>> loadWithCapDimmTempColor(boolean dimmable, boolean temperaturable, boolean colorable);

    @Query("SELECT * FROM " + BaseLight.TABLENAME + " WHERE isDimmable = :dimmable AND isColorable = :colorable")
    LiveData<List<BaseLight>> loadWithCapDimmColor(boolean dimmable,  boolean colorable);

    @Query("SELECT * FROM " + BaseLight.TABLENAME + " WHERE isDimmable = :dimmable  AND isTemperaturable = :temperaturable")
    LiveData<List<BaseLight>> loadWithCapDimmTemp(boolean dimmable, boolean temperaturable);

    @Query("SELECT * FROM " + BaseLight.TABLENAME + " WHERE isDimmable = :dimmable ")
    LiveData<List<BaseLight>> loadWithCapDimm(boolean dimmable);

    @Query("SELECT * FROM " + BaseLight.TABLENAME + " WHERE isTemperaturable = :temperaturable AND isColorable = :colorable")
    LiveData<List<BaseLight>> loadWithCapTempColor(boolean temperaturable, boolean colorable);

    @Query("SELECT * FROM " + BaseLight.TABLENAME + " WHERE isTemperaturable = :temperaturable")
    LiveData<List<BaseLight>> loadWithCapTemp(boolean temperaturable);

    @Query("SELECT * FROM " + BaseLight.TABLENAME + " WHERE  isColorable = :colorable")
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

}
