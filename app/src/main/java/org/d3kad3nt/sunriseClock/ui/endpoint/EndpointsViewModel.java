package org.d3kad3nt.sunriseClock.ui.endpoint;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import kotlin.jvm.functions.Function1;
import org.d3kad3nt.sunriseClock.backend.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.backend.data.repository.EndpointRepository;
import org.d3kad3nt.sunriseClock.backend.data.repository.SettingsRepository;

public class EndpointsViewModel extends AndroidViewModel {

    private final LiveData<List<IEndpointUI>> endpoints;
    private final SettingsRepository settingsRepository;
    private final LiveData<Long> selectedEndpointId;

    public EndpointsViewModel(@NonNull Application application) {
        super(application);
        EndpointRepository endpointRepository =
                EndpointRepository.getInstance(getApplication().getApplicationContext());
        endpoints = endpointRepository.getAllEndpoints();
        settingsRepository = SettingsRepository.getInstance(application.getApplicationContext());
        selectedEndpointId = Transformations.map(settingsRepository.getActiveEndpointIdAsLivedata(), new Function1<>() {
            @Override
            public Long invoke(Optional<Long> input) {
                return input.orElse(-1L);
            }
        });
    }

    public LiveData<List<IEndpointUI>> getEndpoints() {
        return endpoints;
    }

    public void setSelectedEndpoint(final long id) {
        settingsRepository.setActiveEndpoint(id);
    }

    public boolean isSelectedEndpoint(final long id) {
        // getValue() can be null if the LiveData is not yet observed.
        return Objects.equals(selectedEndpointId.getValue(), id);
    }

    public LiveData<Long> getSelectedEndpointId() {
        return selectedEndpointId;
    }
}
