package org.d3kad3nt.sunriseClock.model;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import org.d3kad3nt.sunriseClock.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.model.endpoint.EndpointConfig;
import org.d3kad3nt.sunriseClock.model.endpoint.EndpointConfigDao;
import org.d3kad3nt.sunriseClock.model.endpoint.builder.EndpointBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EndpointRepository {

    private static EndpointConfigDao endpointConfigDao;

    private static Map<Long, LiveData<BaseEndpoint>> endpointLiveDateCache = new HashMap<>();
    private static Map<Long, BaseEndpoint> endpointCache = new HashMap<>();

    private static volatile EndpointRepository INSTANCE;

    private EndpointRepository(Context context){
        endpointConfigDao = AppDatabase.getInstance(context.getApplicationContext()).endpointConfigDao();
    }

    public static EndpointRepository getInstance(Context context){
        if (INSTANCE == null) {
            synchronized (LightRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new EndpointRepository(context);
                }
            }
        }
        return INSTANCE;
    }

    public LiveData<BaseEndpoint> getEndpoint(long id){
        if (!endpointLiveDateCache.containsKey(id)){
            LiveData<BaseEndpoint> endpointTransformation = Transformations.switchMap(endpointConfigDao.loadLiveData(id), input -> {
                if (input == null) {
                    return null;
                } else {
                    return new MutableLiveData<>(createEndpoint(input));
                }
            });
            endpointLiveDateCache.put(id,endpointTransformation);
        }
        return endpointLiveDateCache.get(id);
    }

    BaseEndpoint getSynchronEndpoint(long id){
        if (!endpointCache.containsKey(id)){
            EndpointConfig config = endpointConfigDao.load(id);
            endpointCache.put(id, createEndpoint(config));
        }
        return endpointCache.get(id);
    }

    public LiveData<List<BaseEndpoint>> getAllEndpoints(){
        return Transformations.switchMap(endpointConfigDao.loadAll(), input -> {
            if (input == null) {
                return new MutableLiveData<>(Collections.emptyList());
            } else {
                List<BaseEndpoint> endpoints = new ArrayList<>();
                for (EndpointConfig config : input){
                    endpoints.add(createEndpoint(config));
                }
                return new MutableLiveData<>(endpoints);
            }
        });
    }

    private BaseEndpoint createEndpoint(EndpointConfig config){
        if (config == null){
            throw new NullPointerException("The given config object was null.");
        }
        EndpointBuilder builder = config.type.getBuilder();
        return builder.setConfig(config).build();
    }

}
