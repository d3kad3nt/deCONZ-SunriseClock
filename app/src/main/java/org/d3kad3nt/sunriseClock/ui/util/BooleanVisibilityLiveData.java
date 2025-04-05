package org.d3kad3nt.sunriseClock.ui.util;

import android.view.View;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class BooleanVisibilityLiveData extends androidx.lifecycle.MediatorLiveData<Integer> {

    private final LiveData<Integer> initialVisibilityLivedata;

    private Integer trueVisibility = View.VISIBLE;
    private Integer falseVisibility = View.INVISIBLE;

    public BooleanVisibilityLiveData(Integer initialVisibility) {
        initialVisibilityLivedata = new MutableLiveData<>(initialVisibility);
        this.addSource(initialVisibilityLivedata, integer -> this.setValue(integer));
    }

    public <T> BooleanVisibilityLiveData addVisibilityProvider(LiveData<Boolean> liveData) {
        this.removeSource(this.initialVisibilityLivedata);
        BooleanVisibilityLiveData booleanVisibilityLiveData = this;
        this.addSource(liveData, new Observer<>() {
            @Override
            public void onChanged(Boolean value) {
                if (value) {
                    booleanVisibilityLiveData.setValue(trueVisibility);
                } else {
                    booleanVisibilityLiveData.setValue(falseVisibility);
                }
            }
        });
        return this;
    }

    public BooleanVisibilityLiveData setTrueVisibility(final Integer trueVisibility) {
        this.trueVisibility = trueVisibility;
        return this;
    }

    public BooleanVisibilityLiveData setFalseVisibility(final Integer falseVisibility) {
        this.falseVisibility = falseVisibility;
        return this;
    }
}
