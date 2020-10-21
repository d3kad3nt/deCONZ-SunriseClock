package org.d3kad3nt.sunriseClock.ui.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import org.d3kad3nt.sunriseClock.model.EndpointRepository;
import org.d3kad3nt.sunriseClock.model.SettingsRepository;
import org.d3kad3nt.sunriseClock.model.endpoint.EndpointConfig;

import java.util.List;

import me.ibrahimsn.library.LivePreference;

public class EndpointsViewModel extends AndroidViewModel {
    private final EndpointRepository endpointRepository = EndpointRepository.getInstance(getApplication().getApplicationContext());
    private final SettingsRepository settingsRepository = SettingsRepository.getInstance(getApplication().getApplicationContext());
    private final LiveData<List<EndpointConfig>> endpoints;
    private final LiveData<EndpointConfig> selectedEndpoint;

    public EndpointsViewModel(@NonNull Application application) {
        super(application);
        //TODO use something better
        LivePreference<Long> endpointID = settingsRepository.getLongSetting("endpoint_id",0);
        endpoints = endpointRepository.getEndpointConfigs();
        selectedEndpoint = Transformations.switchMap(endpointID, endpointRepository::getEndpointConfig);
    }

    public LiveData<List<EndpointConfig>> getEndpoints(){
        return endpoints;
    }

    public LiveData<EndpointConfig> getSelectedEndpoint() {
        return selectedEndpoint;
    }
}
