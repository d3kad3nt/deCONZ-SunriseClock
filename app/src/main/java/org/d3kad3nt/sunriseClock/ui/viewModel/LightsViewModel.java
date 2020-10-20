package org.d3kad3nt.sunriseClock.ui.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import org.d3kad3nt.sunriseClock.model.EndpointRepository;
import org.d3kad3nt.sunriseClock.model.LightRepository;
import org.d3kad3nt.sunriseClock.model.SettingsRepository;
import org.d3kad3nt.sunriseClock.model.endpoint.EndpointConfig;
import org.d3kad3nt.sunriseClock.model.endpoint.remoteApi.Resource;
import org.d3kad3nt.sunriseClock.model.light.BaseLight;

import java.util.List;

import me.ibrahimsn.library.LivePreference;

public class LightsViewModel extends AndroidViewModel {
    private static final String TAG = "LightsViewModel";
    private final LightRepository lightRepository = LightRepository.getInstance(getApplication().getApplicationContext());
    private final EndpointRepository endpointRepository = EndpointRepository.getInstance(getApplication().getApplicationContext());
    private final SettingsRepository settingsRepository = SettingsRepository.getInstance(getApplication().getApplicationContext());
    private final LiveData<Resource<List<BaseLight>>> lights;
    private final LiveData<List<EndpointConfig>> endpoints;

    public LightsViewModel(@NonNull Application application) {
        super(application);
        //TODO use something better
        LivePreference<Long> endpointID = settingsRepository.getLongSetting("endpoint_id",0);
        lights = Transformations.switchMap(endpointID, input -> {
            Log.d(TAG, "Switched endpointID to " + input);
            return lightRepository.getLightsForEndpoint(input);
        });
        endpoints = endpointRepository.getEndpointConfigs();
    }

    public LiveData<Resource<List<BaseLight>>> getLights(){
        return lights;
    }
    public LiveData<List<EndpointConfig>> getEndpoints(){
        return endpoints;
    }
}
