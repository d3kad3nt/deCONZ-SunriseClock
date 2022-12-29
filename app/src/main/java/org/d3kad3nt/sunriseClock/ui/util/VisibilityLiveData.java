package org.d3kad3nt.sunriseClock.ui.util;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.d3kad3nt.sunriseClock.data.remote.common.Resource;

import java.util.HashSet;
import java.util.Set;

public class VisibilityLiveData extends androidx.lifecycle.MediatorLiveData<Integer> {

    private final LiveData<Integer> initialVisibilityLivedata;
    private Integer loadingVisibility = View.INVISIBLE;
    private Integer successVisibility = View.VISIBLE;
    private Integer errorVisibility = View.VISIBLE;

    //This uses a raw instance of Resource because the real type is only known in addVisibilityProvider
    @SuppressWarnings("rawtypes")
    private final Set<LiveData<? extends Resource>> loading = new HashSet<>();

    public VisibilityLiveData(Integer initialVisibility){
        initialVisibilityLivedata = new MutableLiveData<>(initialVisibility);
        this.addSource(initialVisibilityLivedata, integer -> this.setValue(integer));
    }

    public <T> VisibilityLiveData addVisibilityProvider(LiveData<? extends Resource<T>> liveData){
        this.removeSource(this.initialVisibilityLivedata);
        VisibilityLiveData visibilityLivedata = this;
        this.addSource(liveData, new Observer<Resource<T>>() {
            @Override
            public void onChanged(Resource<T> resource) {
                switch (resource.getStatus()){
                    case LOADING:
                        loading.add(liveData);
                        visibilityLivedata.setValue(loadingVisibility);
                        break;
                    case SUCCESS:
                        loading.remove(liveData);
                        if (loading.isEmpty()) {
                            visibilityLivedata.setValue(successVisibility);
                        }
                        visibilityLivedata.removeSource(liveData);
                        break;
                    case ERROR:
                        visibilityLivedata.setValue(errorVisibility);
                        visibilityLivedata.removeSource(liveData);
                        break;
                }
            }
        });
        return this;
    }

    public Integer getErrorVisibility() {
        return errorVisibility;
    }

    public VisibilityLiveData setErrorVisibility(Integer errorVisibility) {
        this.errorVisibility = errorVisibility;
        return this;
    }

    public Integer getSuccessVisibility() {
        return successVisibility;
    }

    public VisibilityLiveData setSuccessVisibility(Integer successVisibility) {
        this.successVisibility = successVisibility;
        return this;
    }

    public Integer getLoadingVisibility() {
        return loadingVisibility;
    }

    public VisibilityLiveData setLoadingVisibility(Integer loadingVisibility) {
        this.loadingVisibility = loadingVisibility;
        return this;
    }
}
