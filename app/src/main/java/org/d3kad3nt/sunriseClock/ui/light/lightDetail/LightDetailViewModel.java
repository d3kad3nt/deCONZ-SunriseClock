package org.d3kad3nt.sunriseClock.ui.light.lightDetail;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import org.d3kad3nt.sunriseClock.data.model.light.BaseLight;
import org.d3kad3nt.sunriseClock.data.model.light.LightID;
import org.d3kad3nt.sunriseClock.data.remote.common.Resource;
import org.d3kad3nt.sunriseClock.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.ui.util.LivedataTransformations;
import org.d3kad3nt.sunriseClock.ui.util.VisibilityLiveData;
import org.d3kad3nt.sunriseClock.util.Empty;
import org.d3kad3nt.sunriseClock.util.LiveDataUtil;

public class LightDetailViewModel extends AndroidViewModel {
    private final static String TAG = "LightDetailViewModel";
    private final LightRepository lightRepository = LightRepository.getInstance(getApplication().getApplicationContext());

    public LiveData<Resource<BaseLight>> light;
    public VisibilityLiveData loadingIndicatorVisibility;
    public VisibilityLiveData loadingIndicatorChangeLightOnState;
    public VisibilityLiveData switchLightOnStateVisibility;

    public LightDetailViewModel(@NonNull Application application, LightID lightId) {
        super(application);
        //Todo: Implement something to represent the state of the request inside UI (if (baseLightResource.getStatus().equals(Status.SUCCESS))...)
        //Todo: Data binding in XML has built-in null-safety so viewModel.light.data.friendlyName inside XML works for now (but should be changed?)
        //Todo: Use custom model for UI
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
        LiveData<Resource<Empty>> state = lightRepository.setOnState(light.getValue().getData(), newState);
        loadingIndicatorChangeLightOnState.setVisibilityProvider(state);
    }

    private LiveData<Resource<BaseLight>> getLight(LightID lightID){
        return lightRepository.getLight(lightID);
    }
}
