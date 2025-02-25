package org.d3kad3nt.sunriseClock.ui.light;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import org.d3kad3nt.sunriseClock.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.data.model.resource.EmptyResource;
import org.d3kad3nt.sunriseClock.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.data.repository.EndpointRepository;
import org.d3kad3nt.sunriseClock.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.data.repository.SettingsRepository;
import org.d3kad3nt.sunriseClock.ui.util.ResourceVisibilityLiveData;
import org.d3kad3nt.sunriseClock.util.LogUtil;

import java.util.List;
import java.util.Optional;

public class LightsViewModel extends AndroidViewModel {

    private final LightRepository lightRepository =
        LightRepository.getInstance(getApplication().getApplicationContext());
    private final EndpointRepository endpointRepository =
        EndpointRepository.getInstance(getApplication().getApplicationContext());
    private final SettingsRepository settingsRepository =
        SettingsRepository.getInstance(getApplication().getApplicationContext());
    private final LiveData<Resource<List<UILight>>> lights;
    private final LiveData<List<IEndpointUI>> endpoints;

    public ResourceVisibilityLiveData loadingIndicatorVisibility;

    public LightsViewModel(@NonNull Application application) {
        super(application);

        // Todo: Integrate initial loading of lights.
        loadingIndicatorVisibility = new ResourceVisibilityLiveData(View.INVISIBLE).setLoadingVisibility(View.VISIBLE)
            .setSuccessVisibility(View.INVISIBLE).setErrorVisibility(View.INVISIBLE);

        LiveData<Optional<Long>>endpointID = settingsRepository.getActiveEndpointIdAsLivedata();
        lights = Transformations.switchMap(endpointID, endpointId -> {
            if (endpointId.isEmpty()){
                return new MutableLiveData<>(Resource.success(List.of()));
            } else {
                return lightRepository.getLightsForEndpoint(endpointId.get());
            }
        });
        endpoints = endpointRepository.getAllEndpoints();
    }

    public LiveData<Resource<List<UILight>>> getLights() {
        return lights;
    }

    public LiveData<List<IEndpointUI>> getEndpoints() {
        return endpoints;
    }


    public void setLightOnState(long lightId, boolean newState) {
        LogUtil.d("User toggled setLightOnState with lightId %s to state %s.", lightId, newState);
        LiveData<EmptyResource> state = lightRepository.setOnState(lightId, newState);
        loadingIndicatorVisibility.addVisibilityProvider(state);
    }

    public void setLightBrightness(long lightId, int brightness, final boolean onState) {
        LogUtil.d("Slider for setLightBrightness for lightId %s was set to value %s.", lightId, brightness);
                        //Enable the light if it was disabled
        if (brightness > 0 && !onState){
                lightRepository.setOnState(lightId, true);
        }
        LiveData<EmptyResource> state = lightRepository.setBrightness(lightId, brightness);
        loadingIndicatorVisibility.addVisibilityProvider(state);
    }
}
