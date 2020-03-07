package org.asdfgamer.sunriseClock.model;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import org.asdfgamer.sunriseClock.model.endpoint.BaseEndpoint;
import org.asdfgamer.sunriseClock.model.endpoint.EndpointConfig;
import org.asdfgamer.sunriseClock.model.endpoint.EndpointConfigDao;
import org.asdfgamer.sunriseClock.model.endpoint.EndpointManager;
import org.asdfgamer.sunriseClock.model.endpoint.remoteApi.ApiResponse;
import org.asdfgamer.sunriseClock.model.endpoint.remoteApi.NetworkBoundResource;
import org.asdfgamer.sunriseClock.model.endpoint.remoteApi.Resource;
import org.asdfgamer.sunriseClock.model.light.BaseLight;
import org.asdfgamer.sunriseClock.model.light.BaseLightDao;
import org.asdfgamer.sunriseClock.model.light.ICapability;
import org.asdfgamer.sunriseClock.model.light.Light;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LightRepository {

    private BaseLightDao baseLightDao;

    private EndpointConfigDao endpointConfigDao;

    private EndpointManager endpointManager;

    public LightRepository(Context context){
        baseLightDao = AppDatabase.getInstance(context).baseLightDao();//TODO Overthink usage of Context
        endpointConfigDao = AppDatabase.getInstance(context).endpointConfigDao();//TODO Overthink usage of Context
        endpointManager = EndpointManager.getEndpointManager(null);//TODO null is bad
    }

    public LiveData<Light> getLight(int lightid){
        refreshLight(lightid);
        LiveData<BaseLight>baseLight =  baseLightDao.load(lightid);
        baseLight.observeForever(new endpointAdder(baseLight));//TODO only if not already set
        return (LiveData<Light>) (LiveData<? extends Light>) baseLight;
    }

    public LiveData<List<Light>> getAllLights(){
        return null;
        //TODO: Implement
    }

    private void refreshLight(int lightID){
        //TODO: implement
    }

    public LiveData<List<Light>> getLightByCapability(Class<? extends ICapability>... capabilities ){
        return (LiveData<List<Light>>)(LiveData<? extends List<? extends Light>>) baseLightDao.loadWithCap(capabilities);
    }

    public LightEndpoint getEndpoint(long id) {
        return endpointManager.getEndpoint(id);
    }

    public LightEndpoint createEndpoint(EndpointConfig endpointConfig) {
        return endpointManager.getEndpoint(endpointConfig);
    }

    public LiveData<Resource<List<BaseLight>>> testGetLights(long endpointId) {
        return new NetworkBoundResource<List<BaseLight>, List<BaseLight>>() {
            BaseEndpoint endpoint = endpointManager.getEndpoint(endpointId);

            @NotNull
            @Override
            protected LiveData<ApiResponse<List<BaseLight>>> createCall() {
                return endpoint.getLights();
            }

            @NotNull
            @Override
            protected LiveData<List<BaseLight>> loadFromDb() {
                LiveData<List<BaseLight>> lights =  baseLightDao.loadAllForEndpoint(endpointId);
                //TODO Use endpointAdder
                return lights;
            }

            @Override
            protected boolean shouldFetch(@Nullable List<BaseLight> data) {
                //TODO
                return true;
            }

            @Override
            protected void saveCallResult(List<BaseLight> items) {
                for (BaseLight light : items) {
                    baseLightDao.insertOrUpdate(light);
                }
            }
        }.asLiveData();
    }

    private class endpointAdder implements Observer<BaseLight> {

        private final LiveData<BaseLight> light;

        endpointAdder(LiveData<BaseLight>  light){
            this.light = light;
        }

        @Override
        public void onChanged(BaseLight light) {
            BaseEndpoint endpoint = endpointManager.getEndpoint(light.getEndpointUUID());
            light.observeState(endpoint);
            this.light.removeObserver(this);
        }

    }

}
