package org.d3kad3nt.sunriseClock.ui.endpoint;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.data.repository.EndpointRepository;

import java.util.List;

public class EndpointsViewModel extends AndroidViewModel {

    private final EndpointRepository endpointRepository =
        EndpointRepository.getInstance(getApplication().getApplicationContext());
    private final LiveData<List<IEndpointUI>> endpoints;

    public EndpointsViewModel(@NonNull Application application) {
        super(application);
        //TODO use something better
        endpoints = endpointRepository.getAllEndpoints();
    }

    public LiveData<List<IEndpointUI>> getEndpoints() {
        return endpoints;
    }
}
