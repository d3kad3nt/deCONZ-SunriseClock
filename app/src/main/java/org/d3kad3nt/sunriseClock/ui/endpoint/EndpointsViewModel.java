package org.d3kad3nt.sunriseClock.ui.endpoint;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.data.repository.EndpointRepository;
import org.d3kad3nt.sunriseClock.data.repository.SettingsRepository;

import java.util.List;

public class EndpointsViewModel extends AndroidViewModel {

    private final LiveData<List<IEndpointUI>> endpoints;
    private final SettingsRepository settingsRepository;

    public EndpointsViewModel(@NonNull Application application) {
        super(application);
        EndpointRepository endpointRepository =
            EndpointRepository.getInstance(getApplication().getApplicationContext());
        endpoints = endpointRepository.getAllEndpoints();
        settingsRepository = SettingsRepository.getInstance(application.getApplicationContext());
    }

    public LiveData<List<IEndpointUI>> getEndpoints() {
        return endpoints;
    }

    public void setSelectedEndpoint(final long id) {
        settingsRepository.setActiveEndpoint(id);
        System.out.println(settingsRepository.getActiveEndpoint());
    }

    public boolean isSelectedEndpoint(final long id) {
        try {
            return settingsRepository.getActiveEndpoint() == id;
        } catch (IllegalStateException e){
            return false;
        }
    }
}
