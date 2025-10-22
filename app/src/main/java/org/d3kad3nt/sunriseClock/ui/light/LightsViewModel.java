package org.d3kad3nt.sunriseClock.ui.light;

import android.app.Application;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.d3kad3nt.sunriseClock.backend.data.model.group.UIGroup;
import org.d3kad3nt.sunriseClock.backend.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.EmptyResource;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.backend.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.backend.data.repository.SettingsRepository;
import org.d3kad3nt.sunriseClock.ui.util.ResourceVisibilityLiveData;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class LightsViewModel extends AndroidViewModel {

    private final LightRepository lightRepository =
            LightRepository.getInstance(getApplication().getApplicationContext());
    /** @noinspection FieldCanBeLocal */
    private final SettingsRepository settingsRepository =
            SettingsRepository.getInstance(getApplication().getApplicationContext());

    private final LiveData<Optional<Long>> endpointId;

    private final LiveData<Resource<Map<UIGroup, List<UILight>>>> groupsWithLights;

    /** Whether the loading indicator should be shown by the fragment. */
    public final ResourceVisibilityLiveData loadingIndicatorVisibility;

    /** Whether the loading indicator of the swipeRefreshLayout should be shown by the fragment. */
    public final MediatorLiveData<Boolean> swipeRefreshing = new MediatorLiveData<>(false);

    public LightsViewModel(@NonNull Application application) {
        super(application);

        // Todo: Integrate initial loading of lights.
        loadingIndicatorVisibility = new ResourceVisibilityLiveData(View.INVISIBLE)
                .setLoadingVisibility(View.VISIBLE)
                .setSuccessVisibility(View.INVISIBLE)
                .setErrorVisibility(View.INVISIBLE);

        endpointId = settingsRepository.getActiveEndpointIdAsLivedata();

        groupsWithLights = Transformations.switchMap(endpointId, id -> {
            if (id.isEmpty()) {
                return new MutableLiveData<>(Resource.success(new HashMap<>()));
            } else {
                return lightRepository.getGroupsWithLightsForEndpoint(id.get());
            }
        });
    }

    public void refreshGroupsWithLights() {
        LogUtil.d("User requested refresh of all lights and groups.");

        if (!endpointId.isInitialized()
                || Objects.requireNonNull(endpointId.getValue()).isEmpty()) {
            LogUtil.w("No active endpoint found.");
            return;
        }

        LiveData<EmptyResource> state = lightRepository.refreshGroupsWithLightsForEndpoint(
                endpointId.getValue().get());

        swipeRefreshing.addSource(state, emptyResource -> {
            switch (emptyResource.getStatus()) {
                case SUCCESS, ERROR -> {
                    LogUtil.v("Stopping swipeRefresh animation.");
                    swipeRefreshing.setValue(false);
                    swipeRefreshing.removeSource(state);
                }
                case LOADING -> {
                    LogUtil.v("Starting swipeRefresh animation.");
                    swipeRefreshing.setValue(true);
                }
            }
        });
    }

    public void toggleLightsOnState() {
        LogUtil.d("User requested all lights to be turned on or off.");
        if (!endpointId.isInitialized()
                || Objects.requireNonNull(endpointId.getValue()).isEmpty()) {
            LogUtil.w("No active endpoint found.");
            return;
        }
        LiveData<EmptyResource> state =
                lightRepository.toggleOnStateForEndpoint(endpointId.getValue().get());
        loadingIndicatorVisibility.addVisibilityProvider(state);
    }

    public LiveData<Resource<Map<UIGroup, List<UILight>>>> getGroupsWithLights() {
        return groupsWithLights;
    }

    public void setLightOnState(long lightId, boolean newState) {
        LogUtil.d("User toggled setLightOnState with lightId %s to state %s.", lightId, newState);
        LiveData<EmptyResource> state = lightRepository.setOnState(lightId, newState);
        loadingIndicatorVisibility.addVisibilityProvider(state);
    }

    public void setLightBrightness(long lightId, int brightness, final boolean onState) {
        LogUtil.d("Slider for setLightBrightness for lightId %s was set to value %s.", lightId, brightness);
        // Enable the light if it was disabled
        if (brightness > 0 && !onState) {
            lightRepository.setOnState(lightId, true);
        }
        LiveData<EmptyResource> state = lightRepository.setBrightness(lightId, brightness);
        loadingIndicatorVisibility.addVisibilityProvider(state);
    }

    public void setGroupOnState(final long groupId, final boolean newState) {
        LogUtil.d("User toggled setGroupOnState with groupId %s to state %s.", groupId, newState);
        LiveData<EmptyResource> state = lightRepository.setGroupOnState(groupId, newState);
        loadingIndicatorVisibility.addVisibilityProvider(state);

        // Observe the result of the group toggle operation.
        state.observeForever(new Observer<>() {
            @Override
            public void onChanged(EmptyResource resource) {
                switch (resource.getStatus()) {
                    case SUCCESS -> {
                        if (!endpointId.isInitialized()
                                || Objects.requireNonNull(endpointId.getValue()).isEmpty()) {
                            LogUtil.w("No active endpoint found.");
                            return;
                        }

                        // Todo: Change to refresh only affected lights.
                        LogUtil.d("Group on/off state changed successfully, refreshing all lights for endpoint.");
                        LiveData<EmptyResource> refreshState = lightRepository.refreshLightsForEndpoint(
                                endpointId.getValue().get());
                        loadingIndicatorVisibility.addVisibilityProvider(refreshState);
                        state.removeObserver(this);
                    }
                    case ERROR -> {
                        state.removeObserver(this);
                    }
                }
            }
        });
    }
}
