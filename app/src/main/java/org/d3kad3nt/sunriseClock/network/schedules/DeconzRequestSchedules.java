package org.d3kad3nt.sunriseClock.network.schedules;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.d3kad3nt.sunriseClock.network.DeconzRequest;
import org.d3kad3nt.sunriseClock.network.schedules.model.Schedule;
import org.d3kad3nt.sunriseClock.network.utils.response.custDeserializer.SuccessDeserializer;
import org.d3kad3nt.sunriseClock.network.utils.response.custDeserializer.model.Success;
import org.d3kad3nt.sunriseClock.network.utils.response.genericCallback.DeconzBaseCallbackAdapter;
import org.d3kad3nt.sunriseClock.network.utils.response.genericCallback.SimplifiedCallback;
import org.d3kad3nt.sunriseClock.network.utils.response.genericCallback.SimplifiedCallbackAdapter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Schedules provide the ability to trigger timed commands to groups or lights.
 * This implements some actions for the "schedules/" endpoint.
 *
 * @see <a href="https://dresden-elektronik.github.io/deconz-rest-doc/schedules/">https://dresden-elektronik.github.io/deconz-rest-doc/schedules/</a>
 */
public abstract class DeconzRequestSchedules extends DeconzRequest {

    DeconzRequestSchedules(Uri baseUrl, String apiKey) {
        super(baseUrl, apiKey);
        init();
    }

    private static final String TAG = "DeconzRequestSchedules";

    private SchedulesEndpoint schedulesEndpoint;

    @Override
    public void init() {
        Log.d(TAG, "Init() called.");

        //Set custom Gson deserializer
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Success.class, new SuccessDeserializer())
                .create();
        super.setGsonDeserializer(gson);

        this.schedulesEndpoint = super.createService(SchedulesEndpoint.class);
    }

    /**
     * Creates a new schedule.
     *
     * @param callback The callback to handle the request-related events.
     * @param schedule The schedule object to send to deconz.
     */
    public void createSchedule(CreateScheduleCallback callback, Schedule schedule) {
        Call<Success> call = schedulesEndpoint.createSchedule(schedule);
        call.enqueue(new DeconzBaseCallbackAdapter<>(callback));
    }

    /**
     * Creates a new schedule.
     *
     * @param callback The callback to handle the request-related events.
     * @param schedule The schedule object to send to deconz.
     */
    public void createSchedule(SimplifiedCallback<Success> callback, Schedule schedule) {
        Call<Success> call = schedulesEndpoint.createSchedule(schedule);
        call.enqueue(new SimplifiedCallbackAdapter<>(callback));
    }


    private interface SchedulesEndpoint {

        @POST("schedules/")
        Call<Success> createSchedule(@Body Schedule schedule);
    }
}





