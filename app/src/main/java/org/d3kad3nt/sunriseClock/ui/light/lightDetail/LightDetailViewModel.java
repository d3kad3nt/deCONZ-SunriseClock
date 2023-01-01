package org.d3kad3nt.sunriseClock.ui.light.lightDetail;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.data.model.resource.EmptyResource;
import org.d3kad3nt.sunriseClock.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.data.model.resource.Status;
import org.d3kad3nt.sunriseClock.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.ui.util.VisibilityLiveData;
import org.d3kad3nt.sunriseClock.util.LiveDataUtil;

public class LightDetailViewModel extends AndroidViewModel {
    private final static String TAG = "LightDetailViewModel";
    private final LightRepository lightRepository = LightRepository.getInstance(getApplication().getApplicationContext());
    private final long lightID;

    public LiveData<Resource<UILight>> light;
    public VisibilityLiveData loadingIndicatorVisibility;

    public LightDetailViewModel(@NonNull Application application, long lightId) {
        super(application);
        //Todo: Implement something to represent the state of the request inside UI (if (lightResource.getStatus().equals(Status.SUCCESS))...)
        //Todo: Data binding in XML has built-in null-safety so viewModel.light.data.friendlyName inside XML works for now (but should be changed?)
        this.lightID = lightId;
        light = getLight(lightId);

        loadingIndicatorVisibility = new VisibilityLiveData(View.VISIBLE).setLoadingVisibility(View.VISIBLE).setSuccessVisibility(View.INVISIBLE).setErrorVisibility(View.INVISIBLE).addVisibilityProvider(light);
    }

    public void setLightOnState(boolean newState) {
        LiveDataUtil.observeOnce(light, lightResource -> {
            if (lightResource == null || lightResource.getStatus() == Status.LOADING) {
                return;
            }
            LiveData<EmptyResource> state = lightRepository.setOnState(lightID, newState);
            loadingIndicatorVisibility.addVisibilityProvider(state);

        });
    }

    public void setLightBrightness(int brightness, boolean changedByUser) {
        Log.d(TAG, "Bright: " + brightness + " " + changedByUser);
        if (!changedByUser) {
            return;
        }
        // Todo: double?
        double brightnessPercent = ((double) brightness) / 100;
        LiveData<EmptyResource> state = lightRepository.setBrightness(lightID, brightnessPercent);
        loadingIndicatorVisibility.addVisibilityProvider(state);
    }

    private LiveData<Resource<UILight>> getLight(long lightID) {
        return lightRepository.getLight(lightID);
    }
}
