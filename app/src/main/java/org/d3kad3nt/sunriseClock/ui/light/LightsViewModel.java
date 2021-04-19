package org.d3kad3nt.sunriseClock.ui.light;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import org.d3kad3nt.sunriseClock.data.repository.EndpointRepository;
import org.d3kad3nt.sunriseClock.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.data.repository.SettingsRepository;
import org.d3kad3nt.sunriseClock.data.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.data.remote.common.Resource;
import org.d3kad3nt.sunriseClock.data.model.light.BaseLight;

import java.util.List;

import me.ibrahimsn.library.LivePreference;

public class LightsViewModel extends AndroidViewModel {
    private static final String TAG = "LightsViewModel";
    private final LightRepository lightRepository = LightRepository.getInstance(getApplication().getApplicationContext());
    private final EndpointRepository endpointRepository = EndpointRepository.getInstance(getApplication().getApplicationContext());
    private final SettingsRepository settingsRepository = SettingsRepository.getInstance(getApplication().getApplicationContext());
    private final LiveData<Resource<List<BaseLight>>> lights;
    private final LiveData<List<BaseEndpoint>> endpoints;
    private final LiveData<BaseEndpoint> selectedEndpoint;

    public LightsViewModel(@NonNull Application application) {
        super(application);
        //TODO use something better
        LivePreference<Long> endpointID = settingsRepository.getLongSetting("endpoint_id",0);
        lights = Transformations.switchMap(endpointID, endpointId -> {
            return lightRepository.getLightsForEndpoint(endpointId);
        });
        endpoints = endpointRepository.getAllEndpoints();
        selectedEndpoint = Transformations.switchMap(endpointID, new Function<Long, LiveData<BaseEndpoint>>() {
            @Override
            public LiveData<BaseEndpoint> apply(Long input) {
                return endpointRepository.getEndpoint(input);
            }
        });
    }

    public LiveData<Resource<List<BaseLight>>> getLights(){
        return lights;
    }

    public LiveData<List<BaseEndpoint>> getEndpoints(){
        return endpoints;
    }

    public LiveData<BaseEndpoint> getSelectedEndpoint() {
        return selectedEndpoint;
    }
}
