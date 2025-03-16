package org.d3kad3nt.sunriseClock.ui.util;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.d3kad3nt.sunriseClock.data.model.resource.Resource;

import java.util.HashSet;
import java.util.Set;

public class ResourceVisibilityLiveData extends androidx.lifecycle.MediatorLiveData<Integer> {

    private final LiveData<Integer> initialVisibilityLivedata;

    // This uses a raw instance of Resource because the real type is only known in
    // addVisibilityProvider
    @SuppressWarnings("rawtypes")
    private final Set<LiveData<? extends Resource>> loading = new HashSet<>();

    private Integer loadingVisibility = View.INVISIBLE;
    private Integer successVisibility = View.VISIBLE;
    private Integer errorVisibility = View.VISIBLE;

    public ResourceVisibilityLiveData(Integer initialVisibility) {
        initialVisibilityLivedata = new MutableLiveData<>(initialVisibility);
        this.addSource(initialVisibilityLivedata, integer -> this.setValue(integer));
    }

    public <T> ResourceVisibilityLiveData addVisibilityProvider(
            LiveData<? extends Resource<T>> liveData) {
        this.removeSource(this.initialVisibilityLivedata);
        ResourceVisibilityLiveData resourceVisibilityLivedata = this;
        this.addSource(
                liveData,
                new Observer<Resource<T>>() {
                    @Override
                    public void onChanged(Resource<T> resource) {
                        switch (resource.getStatus()) {
                            case LOADING:
                                loading.add(liveData);
                                resourceVisibilityLivedata.setValue(loadingVisibility);
                                break;
                            case SUCCESS:
                                loading.remove(liveData);
                                if (loading.isEmpty()) {
                                    resourceVisibilityLivedata.setValue(successVisibility);
                                }
                                resourceVisibilityLivedata.removeSource(liveData);
                                break;
                            case ERROR:
                                resourceVisibilityLivedata.setValue(errorVisibility);
                                resourceVisibilityLivedata.removeSource(liveData);
                                break;
                        }
                    }
                });
        return this;
    }

    public Integer getErrorVisibility() {
        return errorVisibility;
    }

    public ResourceVisibilityLiveData setErrorVisibility(Integer errorVisibility) {
        this.errorVisibility = errorVisibility;
        return this;
    }

    public Integer getSuccessVisibility() {
        return successVisibility;
    }

    public ResourceVisibilityLiveData setSuccessVisibility(Integer successVisibility) {
        this.successVisibility = successVisibility;
        return this;
    }

    public Integer getLoadingVisibility() {
        return loadingVisibility;
    }

    public ResourceVisibilityLiveData setLoadingVisibility(Integer loadingVisibility) {
        this.loadingVisibility = loadingVisibility;
        return this;
    }
}
