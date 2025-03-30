/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
 */

package org.d3kad3nt.sunriseClock.util.serviceLocator;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ServiceLocator {

    private static final EnumCache<ExecutorType, Executor> executors = new EnumCache<>();

    static {
        executors.addInstance(ExecutorType.IO, Executors.newSingleThreadExecutor());
        executors.addInstance(ExecutorType.Network, Executors.newFixedThreadPool(3));
        executors.addInstance(ExecutorType.MainThread, new MainThreadExecutor());
    }

    public static Executor getExecutor(ExecutorType type) {
        return executors.getInstance(type);
    }

    private static class MainThreadExecutor implements Executor {

        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
