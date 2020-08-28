package org.d3kad3nt.sunriseClock.model.endpoint;

import android.content.Context;

import org.d3kad3nt.sunriseClock.model.AppDatabase;
import org.d3kad3nt.sunriseClock.model.endpoint.builder.EndpointBuilder;

import java.util.HashMap;
import java.util.Map;

public class EndpointManager {

    private static EndpointManager endpointManager;

    private final EndpointConfigDao configDAO;

    private Map<Long, BaseEndpoint> endpointCache = new HashMap<>();

    public static EndpointManager getEndpointManager(Context context){
        if (endpointManager == null){
            endpointManager = new EndpointManager(context);
        }
        return endpointManager;
    }

    private EndpointManager(Context context){
        AppDatabase database =  AppDatabase.getInstance(context);
        this.configDAO = database.endpointConfigDao();
    }

    public BaseEndpoint getEndpoint(EndpointConfig config){
        if (!endpointCache.containsKey(config.id)){
            endpointCache.put(config.id,createEndpoint(config));
        }
        return endpointCache.get(config.id);
    }

    public BaseEndpoint getEndpoint(long id){
        if (!endpointCache.containsKey(id)){
            EndpointConfig config = getConfig(id);
            endpointCache.put(id,createEndpoint(config));
        }
        return endpointCache.get(id);
    }

    private EndpointConfig getConfig(long id){
        return configDAO.load(id);
    }

    private BaseEndpoint createEndpoint(EndpointConfig config){
        EndpointBuilder builder = config.type.getBuilder();
        return builder.setConfig(config).build();
    }

}
