package org.d3kad3nt.sunriseClock.ui.endpoint.endpointInfo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import org.d3kad3nt.sunriseClock.data.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.data.model.light.ILightUI;
import org.d3kad3nt.sunriseClock.data.model.light.LightUI;
import org.d3kad3nt.sunriseClock.data.repository.EndpointRepository;

public class EndpointInfoViewModel extends AndroidViewModel {
    private final EndpointRepository endpointRepository = EndpointRepository.getInstance(getApplication().getApplicationContext());

    public EndpointInfoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ILightUI> getEndpoint(long endpointID){
        return Transformations.map(endpointRepository.getEndpoint(endpointID), (BaseEndpoint input) ->  {
                return LightUI.from(input);
        });
    }
}
