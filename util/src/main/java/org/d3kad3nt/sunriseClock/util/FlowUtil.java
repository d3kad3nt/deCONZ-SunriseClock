/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
 */

package org.d3kad3nt.sunriseClock.util;

import android.os.Build;
import androidx.annotation.RequiresApi;
import java.util.List;
import java.util.concurrent.Flow;

@RequiresApi(api = Build.VERSION_CODES.R)
public class FlowUtil {

    public static <T> Flow.Publisher<T> publishList(List<T> list) {
        return new Flow.Publisher<>() {
            @Override
            public void subscribe(final Flow.Subscriber<? super T> subscriber) {
                for (T element : list) {
                    subscriber.onNext(element);
                }
                subscriber.onComplete();
            }
        };
    }
}
