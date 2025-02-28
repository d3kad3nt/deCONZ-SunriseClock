package org.d3kad3nt.sunriseClock.util;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class LiveDataUtil {

    public static <T> void logChanges(final String TAG, @NonNull final LiveData<T> liveData) {
        liveData.observeForever(t -> {
            Log.d("LiveDataUtil", "Log Change");
            Log.d(TAG, t.toString());
        });
    }

    public static <T> void observeOnce(@NonNull final LiveData<T> liveData, @NonNull final Action<T> action) {
        liveData.observeForever(new Observer<T>() {
            @Override
            public void onChanged(T t) {
                action.execute(t);
                liveData.removeObserver(this);
            }
        });
    }

    public static <T> void observeUntilNotNull(@NonNull final LiveData<T> light, @NonNull final Action<T> action) {
        light.observeForever(new Observer<T>() {
            @Override
            public void onChanged(final T t) {
                if (t == null) {
                    return;
                }
                light.removeObserver(this);
                action.execute(t);
            }
        });
    }
}
