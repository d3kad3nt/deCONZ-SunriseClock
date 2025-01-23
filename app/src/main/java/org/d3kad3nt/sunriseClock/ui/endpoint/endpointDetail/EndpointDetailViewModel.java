package org.d3kad3nt.sunriseClock.ui.endpoint.endpointDetail;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.data.repository.EndpointRepository;
import org.d3kad3nt.sunriseClock.data.repository.SettingsRepository;
import org.d3kad3nt.sunriseClock.util.LiveDataUtil;

import java.util.concurrent.atomic.AtomicBoolean;

import me.ibrahimsn.library.LivePreference;

public class EndpointDetailViewModel extends AndroidViewModel {

    private final static String TAG = "EndpointDetailViewModel";
    private final EndpointRepository endpointRepository =
        EndpointRepository.getInstance(getApplication().getApplicationContext());
    private  final SettingsRepository settingsRepository =
        SettingsRepository.getInstance(getApplication().getApplicationContext());

    public LiveData<IEndpointUI> endpointConfig;
    private final long endpointId;

    public EndpointDetailViewModel(@NonNull Application application, long endpointId) {
        super(application);
        endpointConfig = getEndpoint(endpointId);

        this.endpointId = endpointId;
    }

    private LiveData<IEndpointUI> getEndpoint(long endpointID) {
        return endpointRepository.getEndpoint(endpointID);
    }

    public boolean deleteEndpoint() {
        endpointRepository.deleteEndpoint(endpointId);
        final AtomicBoolean result = new AtomicBoolean(false);
        LivePreference<Long> selectedEndpoint = settingsRepository.getLongSetting("endpoint_id", 0);
        LiveDataUtil.observeOnce(selectedEndpoint, selectedEndpointId -> {
            Log.d(TAG, "selected id: " + selectedEndpointId + " current id:" + endpointId);
            if (selectedEndpointId != endpointId){
                Log.d(TAG, "delete endpoint");
                endpointRepository.deleteEndpoint(endpointId);
                result.set(true);
            }
        });
        return result.get();
    }
}
