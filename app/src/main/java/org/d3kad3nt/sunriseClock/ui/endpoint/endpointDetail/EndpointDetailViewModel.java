package org.d3kad3nt.sunriseClock.ui.endpoint.endpointDetail;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import org.d3kad3nt.sunriseClock.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.data.repository.EndpointRepository;
import org.d3kad3nt.sunriseClock.data.repository.SettingsRepository;

import java.util.Optional;

import kotlin.jvm.functions.Function1;

public class EndpointDetailViewModel extends ViewModel {

    public static final CreationExtras.Key<EndpointRepository> ENDPOINT_REPOSITORY_KEY = new CreationExtras.Key<>() {};
    private final EndpointRepository endpointRepository;

    public final static CreationExtras.Key<SettingsRepository> SETTINGS_REPOSITORY_KEY = new CreationExtras.Key<>() {};
    private final SettingsRepository settingsRepository;

    public final static CreationExtras.Key<Long> ENDPOINT_ID_KEY = new CreationExtras.Key<>() {};
    private final long endpointID;

    public LiveData<IEndpointUI> endpointConfig;

    public LiveData<Boolean> selected;

    public EndpointDetailViewModel(@NonNull EndpointRepository endpointRepository, @NonNull SettingsRepository settingsRepository, long endpointId) {
        super();
        this.endpointRepository = endpointRepository;
        this.settingsRepository = settingsRepository;
        this.endpointID = endpointId;

        this.endpointConfig = getEndpoint(endpointId);

        selected = Transformations.map(settingsRepository.getActiveEndpointIdAsLivedata(), new Function1<>() {
            @Override
            public Boolean invoke(final Optional<Long> aLong) {
                return aLong.isPresent() && aLong.get().equals(endpointId);
            }
        });
    }

    private LiveData<IEndpointUI> getEndpoint(long endpointID) {
        return endpointRepository.getEndpoint(endpointID);
    }

    static final ViewModelInitializer<EndpointDetailViewModel> initializer = new ViewModelInitializer<>(
        EndpointDetailViewModel.class,
        creationExtras -> {
            EndpointRepository endpointRepository = creationExtras.get(ENDPOINT_REPOSITORY_KEY);
            SettingsRepository settingsRepository = creationExtras.get(SETTINGS_REPOSITORY_KEY);
            Long endpointId = creationExtras.get(ENDPOINT_ID_KEY);
            return new EndpointDetailViewModel(endpointRepository, settingsRepository, endpointId);
        }
    );
}
