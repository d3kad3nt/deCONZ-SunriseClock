package org.d3kad3nt.sunriseClock.ui.home;

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

public class HomeViewModel extends AndroidViewModel {

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

    public HomeViewModel(@NonNull Application application) {
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

        // After the light's state is set, refresh any group that contains this light to update the group's
        // on/off state in the UI (e.g., from "on" to "off").
        state.observeForever(new Observer<>() {
            @Override
            public void onChanged(EmptyResource resource) {
                switch (resource.getStatus()) {
                    case SUCCESS -> {
                        LogUtil.d("Light on/off state changed successfully, refreshing affected groups.");
                        Resource<Map<UIGroup, List<UILight>>> groupsWithLightsResource = groupsWithLights.getValue();
                        if (groupsWithLightsResource != null && groupsWithLightsResource.getData() != null) {
                            groupsWithLightsResource.getData().entrySet().stream()
                                    // Find all groups that contain the light that was just toggled.
                                    .filter(entry ->
                                            entry.getValue().stream().anyMatch(light -> light.getId() == lightId))
                                    // For each of those groups, trigger a refresh.
                                    .forEach(entry -> {
                                        UIGroup groupToRefresh = entry.getKey();
                                        LogUtil.d(
                                                "Refreshing group %d as it contains the toggled light.",
                                                groupToRefresh.getId());
                                        LiveData<EmptyResource> refreshState =
                                                lightRepository.refreshGroup(groupToRefresh.getId());
                                        loadingIndicatorVisibility.addVisibilityProvider(refreshState);
                                    });
                        }
                        state.removeObserver(this);
                    }
                    case ERROR -> {
                        LogUtil.e("Failed to set light on/off state for light %d.", lightId);
                        state.removeObserver(this);
                    }
                }
            }
        });
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

        // After the group's state is set, all lights and groups must be refreshed.
        // Toggling a group affects the state of its individual lights, which in turn can affect the
        // (partially) on/off status of other groups that contain those same lights.
        // A full refresh is the most reliable way to ensure the entire UI is consistent.
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

                        LogUtil.d("Group on/off state changed successfully, refreshing all lights and groups for "
                                + "endpoint.");
                        LiveData<EmptyResource> refreshState = lightRepository.refreshGroupsWithLightsForEndpoint(
                                endpointId.getValue().get());
                        loadingIndicatorVisibility.addVisibilityProvider(refreshState);
                        state.removeObserver(this);
                    }
                    case ERROR -> {
                        LogUtil.e("Failed to set group on/off state for group %d.", groupId);
                        state.removeObserver(this);
                    }
                }
            }
        });
    }
}
