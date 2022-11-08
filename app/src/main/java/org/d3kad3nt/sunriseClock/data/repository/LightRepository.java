package org.d3kad3nt.sunriseClock.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import org.d3kad3nt.sunriseClock.data.local.AppDatabase;
import org.d3kad3nt.sunriseClock.data.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.data.model.endpoint.LightEndpoint;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiEmptyResponse;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiErrorResponse;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiResponse;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiSuccessResponse;
import org.d3kad3nt.sunriseClock.data.remote.common.NetworkBoundResource;
import org.d3kad3nt.sunriseClock.data.remote.common.Resource;
import org.d3kad3nt.sunriseClock.data.remote.common.Status;
import org.d3kad3nt.sunriseClock.data.model.light.BaseLight;
import org.d3kad3nt.sunriseClock.data.local.BaseLightDao;
import org.d3kad3nt.sunriseClock.data.model.light.ICapability;
import org.d3kad3nt.sunriseClock.data.model.light.Light;
import org.d3kad3nt.sunriseClock.data.model.light.LightID;
import org.d3kad3nt.sunriseClock.util.Empty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Repository module for handling data operations (network or local database).
 */
public class LightRepository {

    private static BaseLightDao baseLightDao;
    private final static String TAG = "LightRepository";
    private static volatile LightRepository INSTANCE;
    private final EndpointRepository endpointRepo;

    /**
     * Using singleton pattern as of now. With dependency injection (Dagger, ...) this class could be mocked when unit testing.
     * TODO: Dependency Injection, optional
     */
    private LightRepository (Context context) {
        baseLightDao = AppDatabase.getInstance(context.getApplicationContext()).baseLightDao();
        endpointRepo = EndpointRepository.getInstance(context);
    }

    public static LightRepository getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LightRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LightRepository(context);
                }
            }
        }
        return INSTANCE;
    }

    private void refreshLight(int endpointId, String endpointLightId){
        //TODO: implement, still needed? (as getLight could be called to update light)
    }

    public LiveData<List<Light>> getLightByCapability(Class<? extends ICapability>... capabilities ){
        return (LiveData<List<Light>>)(LiveData<? extends List<? extends Light>>) baseLightDao.loadWithCap(capabilities);
    }

    //TODO: return Light interface instead of raw BaseLight
    public LiveData<Resource<List<BaseLight>>> getLightsForEndpoint(long endpointId) {
        try {
            endpointRepo.getEndpoint(endpointId);
        }catch (NullPointerException e){
            Resource<List<BaseLight>> resource = new Resource<>(Status.ERROR, null, "Endpoint doesn't exist");
            return new MutableLiveData<>(resource);
        }
        return new NetworkBoundResource<List<BaseLight>, List<BaseLight>>() {

            final LiveData<BaseEndpoint> endpoint = endpointRepo.getEndpoint(endpointId);

            @NotNull
            @Override
            protected LiveData<ApiResponse<List<BaseLight>>> createCall() {
                return Transformations.switchMap(endpoint, input -> {
                    return input.getLights();
                });
            }

            @NotNull
            @Override
            protected LiveData<List<BaseLight>> loadFromDb() {
                //TODO: return (LiveData<Light>) (LiveData<? extends Light>) baseLight;
                return baseLightDao.loadAllForEndpoint(endpointId);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<BaseLight> data) {
                //TODO
                return true;
            }

            @Override
            protected void saveCallResult(List<BaseLight> items) {
                for (BaseLight light : items) {
                    baseLightDao.upsert(light);
                }
            }
        }.asLiveData();
    }

    public LiveData<Resource<BaseLight>> getLight(LightID lightID) {
        return getLight(lightID.getEndpointID(), lightID.getEndpointLightID());
    }


    //TODO: return Light interface instead of raw BaseLight
    public LiveData<Resource<BaseLight>> getLight(long endpointId, String endpointLightId) {
        return new NetworkBoundResource<BaseLight, BaseLight>() {
            final LiveData<BaseEndpoint> endpoint = endpointRepo.getEndpoint(endpointId);

            @NotNull
            @Override
            protected LiveData<ApiResponse<BaseLight>> createCall() {
                return Transformations.switchMap(endpoint, input -> {
                    return input.getLight(endpointLightId);
                });
            }

            @NotNull
            @Override
            protected LiveData<BaseLight> loadFromDb() {
                //TODO: return (LiveData<Light>) (LiveData<? extends Light>) baseLight;
                return baseLightDao.load(endpointId, endpointLightId);
            }

            @Override
            protected boolean shouldFetch(@Nullable BaseLight data) {
                //TODO
                return true;
            }

            @Override
            protected void saveCallResult(BaseLight item) {
                baseLightDao.upsert(item);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Empty>> setOnState(BaseLight light, boolean newState){
        LiveData<BaseEndpoint> endpoint = endpointRepo.getEndpoint(light.getEndpointId());
        //TODO: Flatten Transformations, e.g. with MediatorLiveData
        return Transformations.switchMap(endpoint, new Function<BaseEndpoint, LiveData<Resource<Empty>>>() {
            @Override
            public LiveData<Resource<Empty>> apply(BaseEndpoint input) {
                if (input == null){
                    Log.d(TAG, "Endpoint Loading");
                    return new MutableLiveData<>(new Resource<>(Status.LOADING, Empty.getInstance(), ""));
                }
                Log.d(TAG, "Got Endpoint");
                LiveData<Resource<Empty>>responseSetState = toResource(input.setOnState(light.getEndpointLightId(), newState));
                return Transformations.switchMap(responseSetState, new Function<Resource<Empty>, LiveData<Resource<Empty>>>() {
                    @Override
                    public LiveData<Resource<Empty>> apply(Resource<Empty> input) {
                        if (input.getStatus().equals(Status.SUCCESS)){
                            return Transformations.map(getLight(light.getUUID()), new Function<Resource<BaseLight>, Resource<Empty>>() {
                                @Override
                                public Resource<Empty> apply(Resource<BaseLight> input) {
                                    return new Resource<>(input.getStatus(), Empty.getInstance(), input.getMessage());
                                }
                            });
                        }
                        return new MutableLiveData<>(input);                    }
                });
            }
        });
    }

    private <T> LiveData<Resource<Empty>> toResource(LiveData<ApiResponse<T>> response){
        return Transformations.switchMap(response, input -> {
            if (input == null){
                return new MutableLiveData<>(new Resource<>(Status.LOADING, Empty.getInstance(), ""));
            }else{
                if (input instanceof ApiEmptyResponse || input instanceof ApiSuccessResponse){
                    return new MutableLiveData<>(new Resource<>(Status.SUCCESS, Empty.getInstance(), ""));
                }else{
                    return new MutableLiveData<>(new Resource<>(Status.ERROR, Empty.getInstance(), ""));
                }
            }
        });
    }

}
