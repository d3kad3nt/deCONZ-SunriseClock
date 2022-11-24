package org.d3kad3nt.sunriseClock.ui.light.lightDetail;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.data.model.light.Light;
import org.d3kad3nt.sunriseClock.data.remote.common.EmptyResource;
import org.d3kad3nt.sunriseClock.data.remote.common.Resource;
import org.d3kad3nt.sunriseClock.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.ui.util.VisibilityLiveData;

public class LightDetailViewModel extends AndroidViewModel {
    private final static String TAG = "LightDetailViewModel";
    private final LightRepository lightRepository = LightRepository.getInstance(getApplication().getApplicationContext());
    private final long lightID;

    public LiveData<Resource<Light>> light;
    public VisibilityLiveData loadingIndicatorVisibility;
    public VisibilityLiveData loadingIndicatorChangeLightOnState;

    public LightDetailViewModel(@NonNull Application application, long lightId) {
        super(application);
        //Todo: Implement something to represent the state of the request inside UI (if (baseLightResource.getStatus().equals(Status.SUCCESS))...)
        //Todo: Data binding in XML has built-in null-safety so viewModel.light.data.friendlyName inside XML works for now (but should be changed?)
        //Todo: Use custom model for UI
        this.lightID = lightId;
        light = getLight(lightId);

        loadingIndicatorVisibility = new VisibilityLiveData(View.VISIBLE)
                .setLoadingVisibility(View.VISIBLE)
                .setSuccessVisibility(View.INVISIBLE)
                .setErrorVisibility(View.INVISIBLE)
                .setVisibilityProvider(light);

        loadingIndicatorChangeLightOnState = new VisibilityLiveData(View.GONE);
        loadingIndicatorChangeLightOnState.setLoadingVisibility(View.VISIBLE);
        loadingIndicatorChangeLightOnState.setSuccessVisibility(View.GONE);
    }

    public void setLightOnState(boolean newState){
        //Todo: this might be null, repository should work with IDs only (instead of fully fledged objects)
        LiveData<EmptyResource> state = lightRepository.setOnState(lightID, newState);
        loadingIndicatorChangeLightOnState.setVisibilityProvider(state);
    }

    private LiveData<Resource<Light>> getLight(long lightID){
        return lightRepository.getLight(lightID);
    }
}
