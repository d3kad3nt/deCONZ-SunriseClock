package org.asdfgamer.sunriseClock.model.endpoint;

import android.content.Context;

import org.asdfgamer.sunriseClock.model.AppDatabase;
import org.asdfgamer.sunriseClock.model.endpoint.builder.EndpointBuilder;

import java.util.HashMap;
import java.util.Map;

public class EndpointManager {

    private static EndpointManager endpointManager;

    private final EndpointConfigDao configDAO;

    private Map<Integer, BaseEndpoint> endpointCache = new HashMap<Integer, BaseEndpoint>();

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

    public BaseEndpoint getEndpoint(Integer id){
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
