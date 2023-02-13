package org.d3kad3nt.sunriseClock.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.HashSet;
import java.util.Set;

public class AsyncJoinHelper {

    private final static String TAG = "AsyncJoinHelper";
    private final MutableLiveData<Boolean> joinState = new MutableLiveData<>();
    private final Set<Object> observers = new HashSet<>();
    private boolean somethingObserved = false;

    public void addAsyncTask(Object task) {
        somethingObserved = true;
        observers.add(task);
    }

    public void removeAsyncTask(Object task) {
        observers.remove(task);
        if (observers.isEmpty()) {
            joinState.setValue(somethingObserved);
        }
    }

    public void executeWhenJoined(Action action) {
        joinState.observeForever(new androidx.lifecycle.Observer<>() {
            @Override
            public void onChanged(final Boolean completed) {
                if (completed) {
                    action.apply();
                    joinState.removeObserver(this);
                }
            }
        });
    }

    @FunctionalInterface
    public interface Action {

        void apply();
    }

    public static abstract class Observer <T> implements androidx.lifecycle.Observer<T> {

        public Observer(@NonNull AsyncJoinHelper joinHelper) {
            joinHelper.addAsyncTask(this);
        }
    }
}
