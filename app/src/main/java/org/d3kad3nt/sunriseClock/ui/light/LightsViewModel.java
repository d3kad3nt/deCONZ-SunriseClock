package org.d3kad3nt.sunriseClock.ui.light;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import org.d3kad3nt.sunriseClock.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.data.model.resource.EmptyResource;
import org.d3kad3nt.sunriseClock.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.data.repository.SettingsRepository;
import org.d3kad3nt.sunriseClock.ui.util.ResourceVisibilityLiveData;
import org.d3kad3nt.sunriseClock.util.LogUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LightsViewModel extends AndroidViewModel {

    private final LightRepository lightRepository =
            LightRepository.getInstance(getApplication().getApplicationContext());
    private final SettingsRepository settingsRepository =
            SettingsRepository.getInstance(getApplication().getApplicationContext());

    private final LiveData<Optional<Long>> endpointId;

    private final LiveData<Resource<List<UILight>>> lights;

    /** Whether the loading indicator should be shown by the fragment. */
    public ResourceVisibilityLiveData loadingIndicatorVisibility;

    /** Whether the loading indicator of the swipeRefreshLayout should be shown by the fragment. */
    public MediatorLiveData<Boolean> swipeRefreshing = new MediatorLiveData<>(false);

    public LightsViewModel(@NonNull Application application) {
        super(application);

        // Todo: Integrate initial loading of lights.
        loadingIndicatorVisibility =
                new ResourceVisibilityLiveData(View.INVISIBLE)
                        .setLoadingVisibility(View.VISIBLE)
                        .setSuccessVisibility(View.INVISIBLE)
                        .setErrorVisibility(View.INVISIBLE);

        endpointId = settingsRepository.getActiveEndpointIdAsLivedata();

        lights =
                Transformations.switchMap(
                        endpointId,
                        id -> {
                            if (id.isEmpty()) {
                                return new MutableLiveData<>(Resource.success(List.of()));
                            } else {
                                return lightRepository.getLightsForEndpoint(id.get());
                            }
                        });
    }

    public void refreshLights() {
        LogUtil.d("User requested refresh of all lights.");

        if (!endpointId.isInitialized()
                || Objects.requireNonNull(endpointId.getValue()).isEmpty()) {
            LogUtil.w("No active endpoint found.");
            return;
        }

        LiveData<EmptyResource> state =
                lightRepository.refreshLightsForEndpoint(endpointId.getValue().get());

        swipeRefreshing.addSource(
                state,
                emptyResource -> {
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

    public LiveData<Resource<List<UILight>>> getLights() {
        return lights;
    }

    public void setLightOnState(long lightId, boolean newState) {
        LogUtil.d("User toggled setLightOnState with lightId %s to state %s.", lightId, newState);
        LiveData<EmptyResource> state = lightRepository.setOnState(lightId, newState);
        loadingIndicatorVisibility.addVisibilityProvider(state);
    }

    public void setLightBrightness(long lightId, int brightness, final boolean onState) {
        LogUtil.d(
                "Slider for setLightBrightness for lightId %s was set to value %s.",
                lightId, brightness);
        // Enable the light if it was disabled
        if (brightness > 0 && !onState) {
            lightRepository.setOnState(lightId, true);
        }
        LiveData<EmptyResource> state = lightRepository.setBrightness(lightId, brightness);
        loadingIndicatorVisibility.addVisibilityProvider(state);
    }
}
