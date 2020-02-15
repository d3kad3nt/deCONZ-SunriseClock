package org.asdfgamer.sunriseClock.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.asdfgamer.sunriseClock.model.light.BaseLight;
import org.asdfgamer.sunriseClock.model.light.BaseLightDao;
import org.asdfgamer.sunriseClock.model.light.ICapability;

import java.util.List;
import java.util.concurrent.Executor;

public class LightRepository {

    //TODO injection
    private Executor executor;

    private BaseLightDao baseLightDao;

    public LightRepository(){
        baseLightDao = AppDatabase.getInstance(null).dao1();//TODO null is bad
    }

    public LiveData<BaseLight> getLight(int lightid){
        refreshLight(lightid);
        return baseLightDao.load(lightid);
    }

    private void refreshLight(int lightID){
//        TODO implement
    }

    public <T extends ICapability> LiveData<List<T>> getLightByCapability(Class<T> capability){
//        TODO implement
        return null;
    }

}
