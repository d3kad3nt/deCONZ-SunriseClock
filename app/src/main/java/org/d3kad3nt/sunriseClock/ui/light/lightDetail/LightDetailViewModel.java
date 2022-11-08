package org.d3kad3nt.sunriseClock.ui.light.lightDetail;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.data.remote.common.Resource;
import org.d3kad3nt.sunriseClock.data.model.light.BaseLight;
import org.d3kad3nt.sunriseClock.data.model.light.LightID;
import org.d3kad3nt.sunriseClock.util.Empty;

public class LightDetailViewModel extends AndroidViewModel {

    private final static String TAG = "LightDetailViewModel";

    private final LightRepository lightRepository = LightRepository.getInstance(getApplication().getApplicationContext());

    public LightDetailViewModel(@NonNull Application application) {
        super(application);
        //TODO use something better
    }

    public LiveData<Resource<BaseLight>> getLight(LightID lightID){
        return lightRepository.getLight(lightID);
    }

    public LiveData<Resource<Empty>> setLightOnState(BaseLight light, boolean newState){
        Log.d(TAG, "Set Light State");
        return lightRepository.setOnState(light, newState);
    }
}
