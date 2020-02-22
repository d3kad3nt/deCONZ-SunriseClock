package org.asdfgamer.sunriseClock.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.asdfgamer.sunriseClock.model.endpoint.BaseMasterEndpoint;
import org.asdfgamer.sunriseClock.model.light.BaseLight;
import org.asdfgamer.sunriseClock.model.light.BaseLightDao;
import org.asdfgamer.sunriseClock.model.light.ICapability;
import org.asdfgamer.sunriseClock.model.light.Light;

import java.util.List;

public class LightRepository {

    //TODO injection
//    private Executor executor;

    private BaseLightDao baseLightDao;

    public LightRepository(){
        baseLightDao = AppDatabase.getInstance(null).dao1();//TODO null is bad
    }

    public LiveData<Light> getLight(int lightid){
        refreshLight(lightid);
        LiveData<BaseLight>baseLight =  baseLightDao.load(lightid);
        baseLight.observeForever(new tempObserver(baseLight));
        LiveData<? extends Light> tempLight = baseLight;
        return (LiveData<Light>) tempLight;
    }

    private void refreshLight(int lightID){
//        TODO implement
    }

    public <T extends ICapability> LiveData<List<T>> getLightByCapability(Class<T> capability){
//        TODO implement
        return null;
    }

    private class tempObserver implements Observer<BaseLight> {

        private final LiveData<BaseLight> light;

        tempObserver(LiveData<BaseLight>  light){
            this.light = light;
        }

        @Override
        public void onChanged(BaseLight light) {
            BaseLightObserver observer = new BaseMasterEndpoint();
            light.observeState(observer);
            Log.e("Temp Observer", "onChanged: Set Observer");
            this.light.removeObserver(this);

        }
    }

}
