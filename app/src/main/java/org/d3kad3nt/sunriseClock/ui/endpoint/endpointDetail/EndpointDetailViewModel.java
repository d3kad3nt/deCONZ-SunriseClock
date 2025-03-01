package org.d3kad3nt.sunriseClock.ui.endpoint.endpointDetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import org.d3kad3nt.sunriseClock.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.data.repository.EndpointRepository;
import org.d3kad3nt.sunriseClock.data.repository.SettingsRepository;

import java.util.Optional;

import kotlin.jvm.functions.Function1;

public class EndpointDetailViewModel extends AndroidViewModel {

    private final EndpointRepository endpointRepository =
        EndpointRepository.getInstance(getApplication().getApplicationContext());

    private final SettingsRepository settingsRepository =
        SettingsRepository.getInstance(getApplication().getApplicationContext());

    public LiveData<IEndpointUI> endpointConfig;

    public LiveData<Boolean> selected;

    public EndpointDetailViewModel(@NonNull Application application, long endpointId) {
        super(application);
        endpointConfig = getEndpoint(endpointId);
        selected = Transformations.map(settingsRepository.getActiveEndpointIdAsLivedata(), new Function1<>() {
            @Override
            public Boolean invoke(final Optional<Long> aLong) {
                return aLong.isPresent() && aLong.get().equals(endpointId);
            }
        });
    }

    private LiveData<IEndpointUI> getEndpoint(long endpointID) {
        return endpointRepository.getEndpoint(endpointID);
    }
}
