package org.d3kad3nt.sunriseClock.model.endpoint;

import android.content.Context;

import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.model.AppDatabase;
import org.d3kad3nt.sunriseClock.model.LightRepository;

import java.util.List;

public class EndpointRepository {

    private static EndpointConfigDao endpointConfigDao;

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

}
