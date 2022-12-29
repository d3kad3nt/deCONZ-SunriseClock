package org.d3kad3nt.sunriseClock.ui.endpoint.endpointDetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

public class EndpointDetailViewModelFactory implements ViewModelProvider.Factory {
    private final Application mApplication;
    private final long mParam;

    public EndpointDetailViewModelFactory(Application application, long endpointId) {
        mApplication = application;
        mParam = endpointId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return Objects.requireNonNull(modelClass.cast(new EndpointDetailViewModel(mApplication, mParam)));
    }
}
