package org.d3kad3nt.sunriseClock.ui.light;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import org.d3kad3nt.sunriseClock.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.data.model.resource.EmptyResource;
import org.d3kad3nt.sunriseClock.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.data.repository.EndpointRepository;
import org.d3kad3nt.sunriseClock.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.data.repository.SettingsRepository;
import org.d3kad3nt.sunriseClock.ui.util.ResourceVisibilityLiveData;

import java.util.List;

import kotlin.jvm.functions.Function1;
import me.ibrahimsn.library.LivePreference;

public class LightsViewModel extends AndroidViewModel {

    private static final String TAG = "LightsViewModel";
    private final LightRepository lightRepository =
        LightRepository.getInstance(getApplication().getApplicationContext());
    private final EndpointRepository endpointRepository =
        EndpointRepository.getInstance(getApplication().getApplicationContext());
    private final SettingsRepository settingsRepository =
        SettingsRepository.getInstance(getApplication().getApplicationContext());
    private final LiveData<Resource<List<UILight>>> lights;
    private final LiveData<List<IEndpointUI>> endpoints;
    private final LiveData<IEndpointUI> selectedEndpoint;

    public ResourceVisibilityLiveData loadingIndicatorVisibility;

    public LightsViewModel(@NonNull Application application) {
        super(application);

        // Todo: Integrate initial loading of lights.
        loadingIndicatorVisibility = new ResourceVisibilityLiveData(View.INVISIBLE).setLoadingVisibility(View.VISIBLE)
            .setSuccessVisibility(View.INVISIBLE).setErrorVisibility(View.INVISIBLE);

        //TODO use something better
        LivePreference<Long> endpointID = settingsRepository.getLongSetting("endpoint_id", 0);
        lights = Transformations.switchMap(endpointID, endpointId -> {
            return lightRepository.getLightsForEndpoint(endpointId);
        });
        endpoints = endpointRepository.getAllEndpoints();
        selectedEndpoint = Transformations.switchMap(endpointID, new Function1<Long, LiveData<IEndpointUI>>() {
            @Override
            public LiveData<IEndpointUI> invoke(Long input) {
                return endpointRepository.getEndpoint(input);
            }
        });
    }

    public LiveData<Resource<List<UILight>>> getLights() {
        return lights;
    }

    public LiveData<List<IEndpointUI>> getEndpoints() {
        return endpoints;
    }

    public LiveData<IEndpointUI> getSelectedEndpoint() {
        return selectedEndpoint;
    }

    public void setLightOnState(long lightId, boolean newState) {
        Log.d(TAG, String.format("User toggled setLightOnState with lightId %s to state %s.", lightId, newState));
        LiveData<EmptyResource> state = lightRepository.setOnState(lightId, newState);
        loadingIndicatorVisibility.addVisibilityProvider(state);
    }

    public void setLightBrightness(long lightId, int brightness) {
        Log.d(TAG,
            String.format("Slider for setLightBrightness for lightId %s was set to value %s.", lightId, brightness));
        LiveData<EmptyResource> state = lightRepository.setBrightness(lightId, brightness);
        loadingIndicatorVisibility.addVisibilityProvider(state);
    }
}
