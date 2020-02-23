package org.asdfgamer.sunriseClock.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import org.asdfgamer.sunriseClock.model.endpoint.BaseEndpoint;
import org.asdfgamer.sunriseClock.model.light.BaseLight;
import org.asdfgamer.sunriseClock.model.light.BaseLightDao;
import org.asdfgamer.sunriseClock.model.light.ICapability;
import org.asdfgamer.sunriseClock.model.light.ILightRemoteColorable;
import org.asdfgamer.sunriseClock.model.light.Light;

import java.util.List;

public class LightRepository {

    //TODO injection
//    private Executor executor;

    private BaseLightDao baseLightDao;

    public LightRepository(){
        baseLightDao = AppDatabase.getInstance(null).baseLightDao();//TODO null is bad
    }

    public LiveData<Light> getLight(int lightid){
        refreshLight(lightid);
        LiveData<BaseLight>baseLight =  baseLightDao.load(lightid);
        baseLight.observeForever(new endpointAdder(baseLight));//TODO only if not already set
        return (LiveData<Light>) (LiveData<? extends Light>) baseLight;
    }

    public LiveData<List<Light>> getAllLights(){

        LiveData<List<Light>> list = getLightByCapability(ILightRemoteColorable.class);
//        LiveData<List<Light>> list2 = getLightByCapability(ILightRemoteColorable.class,ILightRemoteDimmable.class);
//        list.get(i).requestSetOn();
        return null;
    }

    private void refreshLight(int lightID){
//        TODO implement
    }

    public <T extends ICapability> LiveData<List<Light>> getLightByCapability(Class<T> capability){
        return null;
    }
    public <T extends ICapability> LiveData<List<Light>> getLightByCapability(Class<T> capability, Class<T> capability2){
//        TODO implement
        return null;
    }

    private class endpointAdder implements Observer<BaseLight> {

        private final LiveData<BaseLight> light;

        endpointAdder(LiveData<BaseLight>  light){
            this.light = light;
        }

        @Override
        public void onChanged(BaseLight light) {
            LightEndpoint observer = new BaseEndpoint();
            light.observeState(observer);
            Log.e("Temp Observer", "onChanged: Set Observer");
            this.light.removeObserver(this);
        }
    }

}
