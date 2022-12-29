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
        if (modelClass.equals(LightDetailViewModel.class)){
            //This Cast is checked with the previous if statement.
            //noinspection unchecked
            return (T) new LightDetailViewModel(mApplication, mId);
        }else{
            throw new UnsupportedOperationException("This Factory can only create LightDetailViewModel instances");
        }
    }
}
