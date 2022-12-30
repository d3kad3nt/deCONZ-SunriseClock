package org.d3kad3nt.sunriseClock.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import org.d3kad3nt.sunriseClock.data.local.AppDatabase;
import org.d3kad3nt.sunriseClock.data.local.BaseLightDao;
import org.d3kad3nt.sunriseClock.data.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.data.model.light.BaseLight;
import org.d3kad3nt.sunriseClock.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.data.model.resource.EmptyResource;
import org.d3kad3nt.sunriseClock.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiResponse;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiSuccessResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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

    public LiveData<Resource<List<UILight>>> getLightsForEndpoint(long endpointId) {
        try {
            endpointRepo.getEndpoint(endpointId);
        }catch (NullPointerException e) {
            Resource<List<UILight>> resource = Resource.error("Endpoint doesn't exist",null);
            return new MutableLiveData<>(resource);
        }
        return new NetworkBoundResource<List<UILight>, List<BaseLight>, List<BaseLight>>() {

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

            @Override
            protected List<UILight> convertDbTypeToResultType(List<BaseLight> items) {
                List<UILight> lights = new ArrayList<>();
                for (BaseLight light : items) {
                    lights.add(UILight.from(light));
                }
                return lights;
            }

            @Override
            protected List<BaseLight> convertRemoteTypeToDbType(ApiSuccessResponse<List<BaseLight>> response) {
                return response.getBody();
            }

            @NotNull
            @Override
            protected LiveData<List<BaseLight>> loadFromDb() {
                return Transformations.map(baseLightDao.loadAllForEndpoint(endpointId), input -> {
                    return new ArrayList<>(input);
                });
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

    public LiveData<Resource<UILight>> getBaseLight(long lightId) {
        return new NetworkBoundResource<UILight, BaseLight, BaseLight>() {

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

            @Override
            protected UILight convertDbTypeToResultType(BaseLight item) {
                return UILight.from(item);
            }

            @Override
            protected BaseLight convertRemoteTypeToDbType(ApiSuccessResponse<BaseLight> response) {
                return response.getBody();
            }

            @NotNull
            @Override
            protected LiveData<BaseLight> loadFromDb() {
                return baseLightDao.load(lightId);
            }

            @Override
            protected boolean shouldFetch(@Nullable BaseLight data) {
                //TODO
                return true;
            }

            @Override
            protected void saveNetworkResponseToDb(BaseLight item) {
                // The primary key lightId is not known to the remote endpoint, but it is known to us.
                // Set the lightId to enable direct update/insert via primary key (instead of endpointId and endpointLightId) through Room.
                item.lightId = lightId;
                baseLightDao.upsert(item);
            }
        };
    }

    public LiveData<EmptyResource> setOnState(long lightId, boolean newState) {

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
                return null;
                //return getBaseLight(lightId);
            }
        };
    }

    public LiveData<EmptyResource> setBrightness(long lightId, double brightness) {

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
                return baseEndpoint.setBrightness(dbObject.getEndpointLightId(), brightness);
            }

            @NotNull
            @Override
            protected LiveData<Resource<BaseLight>> loadUpdatedVersion() {
                return null;
                //return getBaseLight(lightId);
            }
        };
    }
}