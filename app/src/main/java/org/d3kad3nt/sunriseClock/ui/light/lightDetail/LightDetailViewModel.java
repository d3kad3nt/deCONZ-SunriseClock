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
import org.d3kad3nt.sunriseClock.util.Empty;
import org.d3kad3nt.sunriseClock.util.LiveDataUtil;

public class LightDetailViewModel extends AndroidViewModel {
    private final static String TAG = "LightDetailViewModel";
    private final LightRepository lightRepository = LightRepository.getInstance(getApplication().getApplicationContext());

    public LiveData<Resource<BaseLight>> light;
    public LiveData<Integer> loadingIndicatorVisibility;
    public MediatorLiveData<Integer> loadingIndicatorChangeLightOnState;
    public MediatorLiveData<Integer> switchLightOnStateVisibility;

    public LiveData<Integer> getLoadingIndicatorChangeLightOnState() {
        return loadingIndicatorChangeLightOnState;
    }

    public LightDetailViewModel(@NonNull Application application, LightID lightId) {
        super(application);
        //Todo: Implement something to represent the state of the request inside UI (if (baseLightResource.getStatus().equals(Status.SUCCESS))...)
        //Todo: Data binding in XML has built-in null-safety so viewModel.light.data.friendlyName inside XML works for now (but should be changed?)
        //Todo: Use custom model for UI
        light = getLight(lightId);
        loadingIndicatorVisibility = Transformations.map(light, (Resource<BaseLight> input) -> LivedataTransformations.visibleWhenLoading(input));
        loadingIndicatorChangeLightOnState = new MediatorLiveData<>();
        loadingIndicatorChangeLightOnState.addSource(
                LivedataTransformations.goneStateLiveData, integer -> {
                    loadingIndicatorChangeLightOnState.setValue(integer);
                });
    }

    public void setLightOnState(boolean newState){
        //Todo: this might be null, repository should work with IDs only (instead of fully fledged objects)
        LiveData<Resource<Empty>> state = lightRepository.setOnState(light.getValue().getData(), newState);
        loadingIndicatorChangeLightOnState.removeSource(LivedataTransformations.goneStateLiveData);
        loadingIndicatorChangeLightOnState.addSource(state, new Observer<Resource<Empty>>() {
            @Override
            public void onChanged(Resource<Empty> emptyResource) {
                loadingIndicatorChangeLightOnState.setValue(
                        LivedataTransformations.changeStateWhenLoading(emptyResource, View.VISIBLE, View.GONE));
            }
        });
    }

    private LiveData<Resource<BaseLight>> getLight(LightID lightID){
        return lightRepository.getLight(lightID);
    }
}
