package org.d3kad3nt.sunriseClock.ui.light.lightDetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class LightDetailViewModelFactory implements ViewModelProvider.Factory {

    private final Application mApplication;
    private final long mId;

    public LightDetailViewModelFactory(Application application, long lightId) {
        mApplication = application;
        mId = lightId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.equals(LightDetailViewModel.class)) {
            T viewModel = modelClass.cast(new LightDetailViewModel(mApplication, mId));
            if (viewModel == null) {
                throw new IllegalStateException("Problem occurred while casting a viewModel");
            }
            return viewModel;
        } else {
            throw new UnsupportedOperationException("This Factory can only create LightDetailViewModel instances");
        }
    }
}
