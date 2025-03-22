package org.d3kad3nt.sunriseClock.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class LiveDataUtil {

    public static <T> void logChanges(String TAG, @NonNull LiveData<T> liveData) {
        liveData.observeForever(t -> {
            LogUtil.d("LiveDataUtil log Change");
            LogUtil.d(t.toString());
        });
    }

    public static <T> void observeOnce(
            @NonNull LiveData<T> liveData, @NonNull Observer<T> observer) {
        liveData.observeForever(new Observer<T>() {
            @Override
            public void onChanged(T t) {
                observer.onChanged(t);
                liveData.removeObserver(this);
            }
        });
    }
}
