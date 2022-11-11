package org.d3kad3nt.sunriseClock.ui.util;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.d3kad3nt.sunriseClock.data.remote.common.Resource;

public class VisibilityLiveData extends androidx.lifecycle.MediatorLiveData<Integer> {

    private final LiveData<Integer> initialVisibilityLivedata;
    private Integer loadingVisibility = View.INVISIBLE;
    private Integer successVisibility = View.VISIBLE;
    private Integer errorVisibility = View.VISIBLE;

    public VisibilityLiveData(Integer initialVisibility){
        initialVisibilityLivedata = new MutableLiveData<>(initialVisibility);
        this.addSource(initialVisibilityLivedata, integer -> this.setValue(integer));
    }



    public <T> void setVisibilityProvider(LiveData<Resource<T>> liveData){
        this.removeSource(this.initialVisibilityLivedata);
        VisibilityLiveData visibilityLivedata = this;
        this.addSource(liveData, new Observer<Resource<T>>() {
            @Override
            public void onChanged(Resource<T> resource) {
                switch (resource.getStatus()){
                    case LOADING:
                        visibilityLivedata.setValue(loadingVisibility);
                        break;
                    case SUCCESS:
                        visibilityLivedata.setValue(successVisibility);
                        break;
                    case ERROR:
                        visibilityLivedata.setValue(errorVisibility);
                        break;
                }
            }
        });
    }

    public Integer getErrorVisibility() {
        return errorVisibility;
    }

    public void setErrorVisibility(Integer errorVisibility) {
        this.errorVisibility = errorVisibility;
    }

    public Integer getSuccessVisibility() {
        return successVisibility;
    }

    public void setSuccessVisibility(Integer successVisibility) {
        this.successVisibility = successVisibility;
    }

    public Integer getLoadingVisibility() {
        return loadingVisibility;
    }

    public void setLoadingVisibility(Integer loadingVisibility) {
        this.loadingVisibility = loadingVisibility;
    }
}
