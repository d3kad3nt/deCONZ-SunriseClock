package org.d3kad3nt.sunriseClock.util;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class LiveDataUtil {

    public static <T> void logChanges(String TAG, LiveData<T> liveData){
        liveData.observeForever(t -> {
            Log.d("LiveDataUtil", "Log Change");
            Log.d(TAG, t.toString());
        });
    }

    public static <T> void observeOnce(LiveData<T> liveData, Observer<T> observer){
        liveData.observeForever(new Observer<T>() {
            @Override
            public void onChanged(T t) {
                observer.onChanged(t);
                liveData.removeObserver(this);
            }
        });
    }

}
