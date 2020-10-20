package org.d3kad3nt.sunriseClock.model;

import android.content.Context;

import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.model.endpoint.EndpointConfig;
import org.d3kad3nt.sunriseClock.model.endpoint.EndpointConfigDao;
import org.d3kad3nt.sunriseClock.model.endpoint.builder.EndpointBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EndpointRepository {

    private static EndpointConfigDao endpointConfigDao;

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

    public LiveData<List<EndpointConfig>> getEndpointConfigs(){
        return endpointConfigDao.loadAll();
    }

    public LiveData<EndpointConfig> getEndpointConfig(long id){
        return endpointConfigDao.loadLiveData(id);
    }

    public BaseEndpoint getEndpoint(long id){
        if (!endpointCache.containsKey(id)){
            EndpointConfig config = endpointConfigDao.load(id);
            endpointCache.put(id, createEndpoint(config));
        }
        return endpointCache.get(id);
    }

    public BaseEndpoint getEndpoint(EndpointConfig config){
        if (!endpointCache.containsKey(config.id)){
            endpointCache.put(config.id, createEndpoint(config));
        }
        return endpointCache.get(config.id);
    }

    private BaseEndpoint createEndpoint(EndpointConfig config){
        if (config == null){
            throw new NullPointerException("The given config object was null.");
        }
        EndpointBuilder builder = config.type.getBuilder();
        return builder.setConfig(config).build();
    }

}
