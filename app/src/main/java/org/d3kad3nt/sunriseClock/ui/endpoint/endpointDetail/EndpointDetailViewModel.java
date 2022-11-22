package org.d3kad3nt.sunriseClock.ui.endpoint.endpointDetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.data.model.endpoint.UIEndpoint;
import org.d3kad3nt.sunriseClock.data.repository.EndpointRepository;

public class EndpointDetailViewModel extends AndroidViewModel {
    private final static String TAG = "EndpointDetailViewModel";
    private final EndpointRepository endpointRepository = EndpointRepository.getInstance(getApplication().getApplicationContext());

    public LiveData<UIEndpoint> endpointConfig;

    public EndpointDetailViewModel(@NonNull Application application, long endpointId) {
        super(application);
        endpointConfig = getEndpoint(endpointId);
    }

    private LiveData<UIEndpoint> getEndpoint(long endpointID){
        return endpointRepository.getEndpoint(endpointID);
    }
}
