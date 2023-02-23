package org.d3kad3nt.sunriseClock.util;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.function.Consumer;

@RequiresApi(api = Build.VERSION_CODES.R)
public class ExtendedPublisher <T> implements Flow.Publisher<T> {

    private final List<Flow.Subscriber<? super T>> subscriberList = new LinkedList<>();
    private final List<ExtendedSubscription> subscriptionList = new LinkedList<>();

    private final boolean cache_publish_values;

    private final List<T> cache = new ArrayList<>();

    /**
     * This class implements the java Flow.Publisher API. It has public methods to publish new values and indicate
     * that all values are sent.
     *
     * @param cache_publish_values This indicates if all published values should be cached until the next complete
     *                             call If the values are cached they are emitted to each subscriber that was added
     *                             after the value was published but before complete was called
     */
    public ExtendedPublisher(boolean cache_publish_values) {
        this.cache_publish_values = cache_publish_values;
    }

    @Override
    public void subscribe(final Flow.Subscriber<? super T> subscriber) {
        ExtendedSubscription subscription = new ExtendedSubscription(subscriber);
        subscriber.onSubscribe(subscription);
        subscriptionList.add(subscription);
        if (cache_publish_values) {
            for (T value : cache) {
                subscriber.onNext(value);
            }
        }
    }

    public void publish(@NonNull T value) {
        subscriptionList.forEach(new Consumer<>() {
            @Override
            public void accept(final ExtendedSubscription subscription) {
                if (subscription.requestedItems > 0) {
                    subscription.subscriber.onNext(value);
                    subscription.requestedItems--;
                    subscription.currentListPos++;
                }
            }
        });
        cache.add(value);
    }

    public void complete() {
        subscriberList.forEach(subscriber -> subscriber.onComplete());
        cache.clear();
    }

    class ExtendedSubscription implements Flow.Subscription {

        private final Flow.Subscriber<? super T> subscriber;
        private int currentListPos = cache_publish_values ? 0 : cache.size();

        private long requestedItems = 0;

        ExtendedSubscription(Flow.Subscriber<? super T> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void request(final long n) {
            requestedItems = n;
            while (currentListPos < cache.size() && requestedItems > 0) {
                subscriber.onNext(cache.get(currentListPos));
                currentListPos++;
                requestedItems--;
            }
        }

        @Override
        public void cancel() {
            subscriberList.remove(subscriber);
        }
    }
}
