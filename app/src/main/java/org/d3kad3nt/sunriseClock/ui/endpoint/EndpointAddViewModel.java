package org.d3kad3nt.sunriseClock.ui.endpoint;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import java.util.Map;
import org.d3kad3nt.sunriseClock.data.repository.EndpointRepository;

public class EndpointAddViewModel extends AndroidViewModel {

    private final EndpointRepository endpointRepository =
            EndpointRepository.getInstance(getApplication().getApplicationContext());

    public EndpointAddViewModel(@NonNull Application application) {
        super(application);
    }

    public boolean createEndpoint(Map<String, String> settings) {
        endpointRepository.createEndpoint(settings);
        return true;
    }
}
