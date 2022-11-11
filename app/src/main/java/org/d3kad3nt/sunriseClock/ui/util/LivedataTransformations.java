package org.d3kad3nt.sunriseClock.ui.util;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.d3kad3nt.sunriseClock.data.remote.common.Resource;
import org.d3kad3nt.sunriseClock.data.remote.common.Status;

public class LivedataTransformations {

    public static final LiveData<Integer> goneStateLiveData = new MutableLiveData<>(View.GONE);
    public static final LiveData<Integer> visibleStateLiveData = new MutableLiveData<>(View.VISIBLE);

    public static <T> Integer visibleWhenLoading(Resource<T> input) {
        if (input.getStatus() == Status.LOADING) {
            return View.VISIBLE;
        }
        else {
            return View.INVISIBLE;
        }
    }

    public static <T> Integer changeStateWhenLoading(Resource<T> input, Integer loadingState, Integer otherState) {
        if (input.getStatus() == Status.LOADING) {
            return loadingState;
        }
        else {
            return otherState;
        }
    }
}