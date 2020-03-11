package org.asdfgamer.sunriseClock.model;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

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

    //public LiveData<Light> getLight(int lightid){
        //refreshLight(lightid);
        //LiveData<BaseLight>baseLight =  baseLightDao.load(lightid);
        //baseLight.observeForever(new lightEndpointAdder(baseLight));//TODO only if not already set
        //return (LiveData<Light>) (LiveData<? extends Light>) baseLight;
    //}


    private void refreshLight(int endpointId, String endpointLightId){
        //TODO: implement, still needed? (as getLight could be called to update light)
    }

    public LiveData<List<Light>> getLightByCapability(Class<? extends ICapability>... capabilities ){
        return (LiveData<List<Light>>)(LiveData<? extends List<? extends Light>>) baseLightDao.loadWithCap(capabilities);
    }

    public LightEndpoint getEndpoint(int id) {
        return endpointManager.getEndpoint(id);
    }

    public LightEndpoint createEndpoint(EndpointConfig endpointConfig) {
        return endpointManager.getEndpoint(endpointConfig);
    }

    //TODO: return Light interface instead of raw BaseLight
    public LiveData<Resource<List<BaseLight>>> getLightsForEndpoint(int endpointId) {
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
                LiveData<List<BaseLight>> livedata =  baseLightDao.loadAllForEndpoint(endpointId);

                // Transform LiveData so that lights have non-null endpoint.

                livedata = Transformations.map(livedata, baseLights -> {
                    for (BaseLight baseLight : baseLights) {
                        baseLight.endpoint = endpointManager.getEndpoint(baseLight.getEndpointId());
                    }
                    return baseLights;
                });

                //TODO: return (LiveData<Light>) (LiveData<? extends Light>) baseLight;
                return livedata;
            }

            @Override
            protected boolean shouldFetch(@Nullable List<BaseLight> data) {
                //TODO
                return true;
            }

            @Override
            protected void saveCallResult(List<BaseLight> items) {
                for (BaseLight light : items) {
                    baseLightDao.upsert(light);
                }
            }
        }.asLiveData();
    }

    //TODO: return Light interface instead of raw BaseLight
    public LiveData<Resource<BaseLight>> getLight(int endpointId, String endpointLightId) {
        return new NetworkBoundResource<BaseLight, BaseLight>() {
            BaseEndpoint endpoint = endpointManager.getEndpoint(endpointId);

            @Override
            protected LiveData<ApiResponse<BaseLight>> createCall() {
                return endpoint.getLight(endpointLightId);
            }

            @NotNull
            @Override
            protected LiveData<BaseLight> loadFromDb() {
                LiveData<BaseLight> livedata =  baseLightDao.load(endpointId, endpointLightId);

                // Transform LiveData so that lights have non-null endpoint.
                livedata = Transformations.map(livedata, baseLight -> {
                    baseLight.endpoint = endpointManager.getEndpoint(baseLight.getEndpointId());
                    return baseLight;
                });

                //TODO: return (LiveData<Light>) (LiveData<? extends Light>) baseLight;
                return livedata;
            }

            @Override
            protected boolean shouldFetch(@Nullable BaseLight data) {
                //TODO
                return true;
            }

            @Override
            protected void saveCallResult(BaseLight item) {
                baseLightDao.upsert(item);
            }
        }.asLiveData();
    }


    //TODO: propably replaced by direct transformation of LiveData
    private class lightEndpointAdder implements Observer<BaseLight> {

        private final LiveData<BaseLight> light;

        lightEndpointAdder(LiveData<BaseLight>  light){
            this.light = light;
        }

        @Override
        public void onChanged(BaseLight light) {
            BaseEndpoint endpoint = endpointManager.getEndpoint(light.getEndpointId());
            light.observeState(endpoint);
            this.light.removeObserver(this);
        }

    }


}
