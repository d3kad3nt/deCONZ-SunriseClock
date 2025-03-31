package org.d3kad3nt.sunriseClock.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is used to execute a Action after one ore multiple Tasks are finished.
 *
 * <p>You can set a Action that should be executed by calling executeWhenJoined. This action gets executed, when all
 * registered Async Tasks are finished/removed.
 *
 * <p>This class also contains a subclass of androidx.lifecycle.Observer<T> that adds the observer as a Task in the
 * constructor. This is done, to prevent that a Observer Task isn't added, when the task should be added in the first
 * call of the observer an the observer doesn'T get called in time.
 */
public class AsyncJoin {

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

    public abstract static class Observer<T> implements androidx.lifecycle.Observer<T> {

        public Observer(@NonNull AsyncJoin joinHelper) {
            joinHelper.addAsyncTask(this);
        }
    }
}
