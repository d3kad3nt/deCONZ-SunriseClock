package org.d3kad3nt.sunriseClock.backend.data.remote.deconz;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;
import org.d3kad3nt.sunriseClock.backend.data.remote.common.ApiResponse;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A Retrofit adapter that converts the Call into a LiveData of ApiResponse.
 *
 * <p>Adapted from the official Google architecture-components github-sample app under
 * https://github.com/android/architecture-components-samples/blob/master/GithubBrowserSample/app/src/main/java/com
 * /android/example/github/util/LiveDataCallAdapter.kt.
 */
class LiveDataCallAdapter<T> implements CallAdapter<T, LiveData<ApiResponse<T>>> {

    private final Type responseType;

    LiveDataCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @NonNull
    @Override
    public Type responseType() {
        return responseType;
    }

    @NonNull
    @Override
    public LiveData<ApiResponse<T>> adapt(@NonNull Call<T> call) {
        return new LiveData<>() {
            final AtomicBoolean started = new AtomicBoolean(false);

            @Override
            protected void onActive() {
                super.onActive();
                if (started.compareAndSet(false, true)) {
                    call.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                            postValue(ApiResponse.create(response));
                        }

                        @Override
                        public void onFailure(@NonNull Call<T> call, @NonNull Throwable throwable) {
                            postValue(ApiResponse.create(throwable));
                        }
                    });
                }
            }
        };
    }
}
