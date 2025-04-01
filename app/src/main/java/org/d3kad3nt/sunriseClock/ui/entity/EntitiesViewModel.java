package org.d3kad3nt.sunriseClock.ui.entity;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.d3kad3nt.sunriseClock.backend.data.model.group.UIGroup;
import org.d3kad3nt.sunriseClock.backend.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.backend.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.backend.data.repository.SettingsRepository;
import org.d3kad3nt.sunriseClock.ui.util.ResourceVisibilityLiveData;

public class EntitiesViewModel extends ViewModel {
    public static final CreationExtras.Key<LightRepository> LIGHT_REPOSITORY_KEY = new CreationExtras.Key<>() {};
    public static final CreationExtras.Key<SettingsRepository> SETTINGS_REPOSITORY_KEY = new CreationExtras.Key<>() {};

    static final ViewModelInitializer<EntitiesViewModel> initializer =
        new ViewModelInitializer<>(EntitiesViewModel.class, creationExtras -> {
            LightRepository lightRepository = creationExtras.get(LIGHT_REPOSITORY_KEY);
            SettingsRepository settingsRepository = creationExtras.get(SETTINGS_REPOSITORY_KEY);
            return new EntitiesViewModel(lightRepository, settingsRepository);
        });
    private final LightRepository lightRepository;
    private final SettingsRepository settingsRepository;

    private final LiveData<Optional<Long>> endpointId;

    private final LiveData<Resource<List<UILight>>> lights;
    private final LiveData<Resource<List<UIGroup>>> groups;

    /** Whether the loading indicator should be shown by the fragment. */
    public ResourceVisibilityLiveData loadingIndicatorVisibility;

    /** Whether the loading indicator of the swipeRefreshLayout should be shown by the fragment. */
    public MediatorLiveData<Boolean> swipeRefreshing = new MediatorLiveData<>(false);

    public EntitiesViewModel(@NonNull LightRepository lightRepository, SettingsRepository settingsRepository) {
        super();
        this.lightRepository = lightRepository;
        this.settingsRepository = settingsRepository;

        loadingIndicatorVisibility = new ResourceVisibilityLiveData(View.VISIBLE)
            .setLoadingVisibility(View.VISIBLE)
            .setSuccessVisibility(View.INVISIBLE)
            .setErrorVisibility(View.INVISIBLE);

        endpointId = settingsRepository.getActiveEndpointIdAsLivedata();

        lights = Transformations.switchMap(endpointId, id -> {
            if (id.isEmpty()) {
                return new MutableLiveData<>(Resource.success(new ArrayList<>()));
            } else {
                return lightRepository.getLightsForEndpoint(id.get());
            }
        });

        groups = Transformations.switchMap(endpointId, id -> {
            if (id.isEmpty()) {
                return new MutableLiveData<>(Resource.success(new ArrayList<>()));
            } else {
                return lightRepository.getGroupsForEndpoint(id.get());
            }
        });
    }

    public LiveData<Resource<List<UILight>>> getLights() {
        return lights;
    }

    public LiveData<Resource<List<UIGroup>>> getGroups() {
        return groups;
    }
}
