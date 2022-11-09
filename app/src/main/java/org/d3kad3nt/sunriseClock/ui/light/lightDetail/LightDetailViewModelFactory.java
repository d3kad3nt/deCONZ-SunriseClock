package org.d3kad3nt.sunriseClock.ui.light.lightDetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.d3kad3nt.sunriseClock.data.model.light.LightID;

import java.util.Objects;

public class LightDetailViewModelFactory implements ViewModelProvider.Factory {
    private final Application mApplication;
    private final LightID mParam;

    public LightDetailViewModelFactory(Application application, LightID lightId) {
        mApplication = application;
        mParam = lightId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return Objects.requireNonNull(modelClass.cast(new LightDetailViewModel(mApplication, mParam)));
    }
}
