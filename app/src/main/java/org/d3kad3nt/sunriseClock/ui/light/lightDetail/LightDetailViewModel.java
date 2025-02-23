package org.d3kad3nt.sunriseClock.ui.light.lightDetail;

import android.app.Application;
import android.view.View;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import org.d3kad3nt.sunriseClock.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.data.model.resource.EmptyResource;
import org.d3kad3nt.sunriseClock.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.data.model.resource.Status;
import org.d3kad3nt.sunriseClock.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.ui.util.BooleanVisibilityLiveData;
import org.d3kad3nt.sunriseClock.ui.util.ResourceVisibilityLiveData;
import org.d3kad3nt.sunriseClock.util.LiveDataUtil;

import kotlin.jvm.functions.Function1;

public class LightDetailViewModel extends AndroidViewModel {

    private final static String TAG = "LightDetailViewModel";
    private final LightRepository lightRepository =
        LightRepository.getInstance(getApplication().getApplicationContext());
    private final long lightID;

    public LiveData<Resource<UILight>> light;
    public ResourceVisibilityLiveData loadingIndicatorVisibility;

    public BooleanVisibilityLiveData notReachableCardVisibility;

    public LightDetailViewModel(@NonNull Application application, long lightId) {
        super(application);
        this.lightID = lightId;
        light = getLight(lightId);

        loadingIndicatorVisibility = new ResourceVisibilityLiveData(View.VISIBLE).setLoadingVisibility(View.VISIBLE)
            .setSuccessVisibility(View.INVISIBLE).setErrorVisibility(View.INVISIBLE).addVisibilityProvider(light);

        notReachableCardVisibility =
            new BooleanVisibilityLiveData(View.GONE).setTrueVisibility(View.GONE).setFalseVisibility(View.VISIBLE)
                .addVisibilityProvider(getIsReachable());
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

    public void setLightBrightness(@IntRange(from = 0, to = 100) int brightness, boolean changedByUser) {
        if (!changedByUser) {
            return;
        }
        LiveData<EmptyResource> state = lightRepository.setBrightness(lightID, brightness);
        loadingIndicatorVisibility.addVisibilityProvider(state);
    }

    private LiveData<Resource<UILight>> getLight(long lightID) {
        return lightRepository.getLight(lightID);
    }

    private LiveData<Boolean> getIsReachable() {
        return Transformations.map(light, new Function1<>() {
            @Override
            public Boolean invoke(final Resource<UILight> input) {
                if (input.getStatus() == Status.SUCCESS) {
                    return input.getData().getIsReachable();
                }
                return true;
            }
        });
    }
}
