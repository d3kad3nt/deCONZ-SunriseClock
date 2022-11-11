package org.d3kad3nt.sunriseClock.util;

import android.util.Log;

import androidx.lifecycle.LiveData;

public class LiveDataUtil {

    public static <T> void logChanges(String TAG, LiveData<T> liveData){
        liveData.observeForever(t -> {
            Log.d("LiveDataUtil", "Log Change");
            Log.d(TAG, t.toString());
        });
    }

}
