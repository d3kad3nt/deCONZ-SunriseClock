package org.d3kad3nt.sunriseClock.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.d3kad3nt.sunriseClock.data.local.AppDatabase;
import org.d3kad3nt.sunriseClock.data.local.BaseLightDao;
import org.d3kad3nt.sunriseClock.data.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.data.model.light.BaseLight;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiResponse;
import org.d3kad3nt.sunriseClock.data.remote.common.EmptyResource;
import org.d3kad3nt.sunriseClock.data.remote.common.NetworkBoundResource;
import org.d3kad3nt.sunriseClock.data.remote.common.NetworkUpdateResource;
import org.d3kad3nt.sunriseClock.data.remote.common.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import okhttp3.ResponseBody;

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

    //TODO: return Light interface instead of raw BaseLight
    public LiveData<Resource<List<BaseLight>>> getLightsForEndpoint(long endpointId) {
        try {
            endpointRepo.getEndpoint(endpointId);
        }catch (NullPointerException e){
            Resource<List<BaseLight>> resource = Resource.error("Endpoint doesn't exist",null);
            return new MutableLiveData<>(resource);
        }
        return new NetworkBoundResource<List<BaseLight>, List<BaseLight>>() {

            @NonNull
            @Override
            protected LiveData<BaseEndpoint> loadEndpoint() {
                return endpointRepo.getRepoEndpoint(endpointId);
            }

            @NotNull
            @Override
            protected LiveData<ApiResponse<List<BaseLight>>> loadFromNetwork() {
                return endpoint.getLights();
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
            protected void saveNetworkResponseToDb(List<BaseLight> items) {
                for (BaseLight light : items) {
                    baseLightDao.upsert(light);
                }
            }
        };
    }

    //TODO: return Light interface instead of raw BaseLight
    public LiveData<Resource<BaseLight>> getLight(long lightId) {
        return new NetworkBoundResource<BaseLight, BaseLight>() {

            @NonNull
            @Override
            protected LiveData<BaseEndpoint> loadEndpoint() {
                return endpointRepo.getRepoEndpoint(dbObject.getEndpointId());
            }

            @NotNull
            @Override
            protected LiveData<ApiResponse<BaseLight>> loadFromNetwork() {
                return endpoint.getLight(dbObject.getEndpointLightId());
            }

            @NotNull
            @Override
            protected LiveData<BaseLight> loadFromDb() {
                //TODO: return (LiveData<Light>) (LiveData<? extends Light>) baseLight;
                return baseLightDao.load(lightId);
            }

            @Override
            protected boolean shouldFetch(@Nullable BaseLight data) {
                //TODO
                return true;
            }

            @Override
            protected void saveNetworkResponseToDb(BaseLight item) {
                baseLightDao.upsert(item);
            }
        };
    }

    public LiveData<EmptyResource> setOnState(long lightId, boolean newState){

        return new NetworkUpdateResource<ResponseBody, BaseLight>() {

            @Override
            protected LiveData<BaseLight> loadFromDB() {
                return baseLightDao.load(lightId);
            }

            @Override
            protected LiveData<BaseEndpoint> loadEndpoint() {
                return endpointRepo.getRepoEndpoint(dbObject.getEndpointId());
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ResponseBody>> sendNetworkRequest(BaseEndpoint baseEndpoint) {
                return baseEndpoint.setOnState(dbObject.getEndpointLightId(), newState);
            }

            @NotNull
            @Override
            protected LiveData<Resource<BaseLight>> loadUpdatedVersion() {
                return getLight(lightId);
            }
        };
    }

}