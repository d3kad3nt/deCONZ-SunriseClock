package org.d3kad3nt.sunriseClock.serviceLocator;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ServiceLocator {

    private static EnumCache<ExecutorType,Executor> executors = new EnumCache<>();

    static {
        executors.addInstance(ExecutorType.IO,Executors.newSingleThreadExecutor());
        executors.addInstance(ExecutorType.Network, Executors.newFixedThreadPool(3));
        executors.addInstance(ExecutorType.MainThread, new MainThreadExecutor());
    }

    public static Executor getExecutor(ExecutorType type){
        return executors.getInstance(type);
    }

    private static class MainThreadExecutor implements Executor{
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }

}
