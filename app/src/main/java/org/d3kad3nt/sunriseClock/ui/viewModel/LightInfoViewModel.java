package org.d3kad3nt.sunriseClock.ui.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.model.LightRepository;
import org.d3kad3nt.sunriseClock.model.endpoint.remoteApi.Resource;
import org.d3kad3nt.sunriseClock.model.light.BaseLight;

import java.util.List;

public class LightInfoViewModel extends AndroidViewModel {

    private final LightRepository lightRepository = LightRepository.getInstance(getApplication().getApplicationContext());

    private LiveData<Resource<List<BaseLight>>> lights;

    public LightInfoViewModel(@NonNull Application application) {
        super(application);
        //TODO use something better
        lights = lightRepository.getLightsForEndpoint(5L);
    }

    public LiveData<Resource<List<BaseLight>>> getLight(){
        return lights;
    }
}
