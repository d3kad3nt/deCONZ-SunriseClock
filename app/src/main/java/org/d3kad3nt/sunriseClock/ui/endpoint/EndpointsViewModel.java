package org.d3kad3nt.sunriseClock.ui.endpoint;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import org.d3kad3nt.sunriseClock.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.data.repository.EndpointRepository;
import org.d3kad3nt.sunriseClock.data.repository.SettingsRepository;

import java.util.List;

import me.ibrahimsn.library.LivePreference;

public class EndpointsViewModel extends AndroidViewModel {
    private final EndpointRepository endpointRepository = EndpointRepository.getInstance(getApplication().getApplicationContext());
    private final SettingsRepository settingsRepository = SettingsRepository.getInstance(getApplication().getApplicationContext());
    private final LiveData<List<IEndpointUI>> endpoints;
    private final LiveData<IEndpointUI> selectedEndpoint;

    public EndpointsViewModel(@NonNull Application application) {
        super(application);
        //TODO use something better
        LivePreference<Long> endpointID = settingsRepository.getLongSetting("endpoint_id", 0);
        endpoints = endpointRepository.getAllEndpoints();
        selectedEndpoint = Transformations.switchMap(endpointID, new Function<Long, LiveData<IEndpointUI>>() {
            @Override
            public LiveData<IEndpointUI> apply(Long input) {
                return endpointRepository.getEndpoint(input);
            }
        });
    }

    public LiveData<List<IEndpointUI>> getEndpoints() {
        return endpoints;
    }

    public LiveData<IEndpointUI> getSelectedEndpoint() {
        return selectedEndpoint;
    }
}
