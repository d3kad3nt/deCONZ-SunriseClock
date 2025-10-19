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
    private final LiveData<Long> activeEndpointId;

    public EndpointsViewModel(@NonNull Application application) {
        super(application);
        EndpointRepository endpointRepository =
                EndpointRepository.getInstance(getApplication().getApplicationContext());
        endpoints = endpointRepository.getAllEndpoints();
        settingsRepository = SettingsRepository.getInstance(application.getApplicationContext());
        activeEndpointId = Transformations.map(settingsRepository.getActiveEndpointIdAsLivedata(), new Function1<>() {
            @Override
            public Long invoke(Optional<Long> input) {
                return input.orElse(-1L);
            }
        });
    }

    public LiveData<List<IEndpointUI>> getEndpoints() {
        return endpoints;
    }

    /**
     * Checks if the given endpoint ID is the currently active one.
     *
     * <p>Note: This method might return {@code false} if the underlying {@link LiveData} for the active endpoint has
     * not been observed and initialized yet. For a reactive approach, observe {@link #getActiveEndpointId()} instead.
     *
     * @param id The unique identifier of the endpoint to check.
     * @return {@code true} if the provided ID matches the active endpoint's ID, {@code false} otherwise.
     */
    public boolean isActiveEndpoint(final long id) {
        // getValue() can be null if the LiveData is not yet observed.
        return Objects.equals(activeEndpointId.getValue(), id);
    }

    /**
     * Gets the active endpoint ID as a {@link LiveData} object.
     *
     * <p>This allows observing changes to the active endpoint ID. The value will be updated whenever a new endpoint is
     * set as active. If no endpoint is active, the LiveData will hold the value -1L.
     *
     * @return A {@link LiveData} object containing the ID of the currently active endpoint, or -1L if none is active.
     */
    public LiveData<Long> getActiveEndpointId() {
        return activeEndpointId;
    }

    /**
     * Sets the provided endpoint ID as the currently active one in the settings. This will be persisted and used by
     * other parts of the application to communicate with the correct endpoint.
     *
     * @param id The unique identifier of the endpoint to set as active.
     */
    public void setActiveEndpointId(final long id) {
        settingsRepository.setActiveEndpoint(id);
    }
}
