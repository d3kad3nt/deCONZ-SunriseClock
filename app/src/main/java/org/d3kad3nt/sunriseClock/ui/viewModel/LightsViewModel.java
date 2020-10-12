package org.d3kad3nt.sunriseClock.ui.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.model.LightRepository;
import org.d3kad3nt.sunriseClock.model.endpoint.EndpointConfig;
import org.d3kad3nt.sunriseClock.model.endpoint.EndpointRepository;
import org.d3kad3nt.sunriseClock.model.endpoint.remoteApi.Resource;
import org.d3kad3nt.sunriseClock.model.light.BaseLight;

import java.util.List;

public class LightsViewModel extends AndroidViewModel {
    private final LightRepository lightRepository = LightRepository.getInstance(getApplication().getApplicationContext());
    private final EndpointRepository endpointRepository = EndpointRepository.getInstance(getApplication().getApplicationContext());

    private final LiveData<Resource<List<BaseLight>>> lights;
    private final LiveData<List<EndpointConfig>> endpoints;

    public LightsViewModel(@NonNull Application application) {
        super(application);
        //TODO use something better
        lights = lightRepository.getLightsForEndpoint(5L);
        endpoints = endpointRepository.getEndpointConfigs();
    }

    public LiveData<Resource<List<BaseLight>>> getLights(){
        return lights;
    }
    public LiveData<List<EndpointConfig>> getEndpoints(){
        return endpoints;
    }
}
