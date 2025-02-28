package org.d3kad3nt.sunriseClock.data.repository;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import org.d3kad3nt.sunriseClock.data.local.AppDatabase;
import org.d3kad3nt.sunriseClock.data.local.DbLightDao;
import org.d3kad3nt.sunriseClock.data.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.data.model.light.DbLight;
import org.d3kad3nt.sunriseClock.data.model.light.RemoteLight;
import org.d3kad3nt.sunriseClock.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.data.model.resource.EmptyResource;
import org.d3kad3nt.sunriseClock.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiResponse;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiSuccessResponse;
import org.d3kad3nt.sunriseClock.util.LogUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

/**
 * Repository module for handling data operations (network or local database).
 */
public class LightRepository {

    private static DbLightDao dbLightDao;
    private static volatile LightRepository INSTANCE;
    private final EndpointRepository endpointRepo;

    /**
     * Using singleton pattern as of now. With dependency injection (Dagger, ...) this class could be mocked when unit
     * testing.
     * TODO: Dependency Injection, optional
     */
    private LightRepository(Context context) {
        dbLightDao = AppDatabase.getInstance(context.getApplicationContext()).dbLightDao();
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
        }
        catch (NullPointerException e) {
            Resource<List<UILight>> resource = Resource.error("Endpoint doesn't exist", null);
            return new MutableLiveData<>(resource);
        }
        return new NetworkBoundResource<List<UILight>, List<RemoteLight>, List<DbLight>>() {

            @Override
            protected void saveResponseToDb(List<DbLight> items) {
                for (DbLight light : items) {
                    dbLightDao.upsert(light);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<DbLight> data) {
                //TODO
                return true;
            }

            @NonNull
            @Override
            protected LiveData<BaseEndpoint> loadEndpoint() {
                return endpointRepo.getRepoEndpoint(endpointId);
            }

            @NotNull
            @Override
            protected LiveData<List<DbLight>> loadFromDb() {
                return Transformations.map(dbLightDao.loadAllForEndpoint(endpointId), input -> {
                    return new ArrayList<>(input);
                });
            }

            @NotNull
            @Override
            protected LiveData<ApiResponse<List<RemoteLight>>> loadFromNetwork() {
                return endpoint.getLights();
            }

            @Override
            protected List<UILight> convertDbTypeToResultType(List<DbLight> items) {
                List<UILight> lights = new ArrayList<>();
                for (DbLight light : items) {
                    lights.add(UILight.from(light));
                }
                return lights;
            }

            @Override
            protected List<DbLight> convertRemoteTypeToDbType(ApiSuccessResponse<List<RemoteLight>> response) {
                List<DbLight> lights = new ArrayList<>();
                for (RemoteLight light : response.getBody()) {
                    lights.add(DbLight.from(light));
                }
                return lights;
            }
        };
    }

    public LiveData<Resource<UILight>> getLight(long lightId) {
        return new NetworkBoundResource<UILight, RemoteLight, DbLight>() {

            @Override
            protected void saveResponseToDb(DbLight item) {
                // The primary key lightId is not known to the remote endpoint, but it is known to us.
                // Set the lightId to enable direct update/insert via primary key (instead of endpointId and
                // endpointLightId) through Room.
                item.setLightId(lightId);
                dbLightDao.upsert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable DbLight data) {
                //TODO
                return true;
            }

            @NonNull
            @Override
            protected LiveData<BaseEndpoint> loadEndpoint() {
                return endpointRepo.getRepoEndpoint(dbObject.getEndpointId());
            }

            @NotNull
            @Override
            protected LiveData<DbLight> loadFromDb() {
                return dbLightDao.load(lightId);
            }

            @NotNull
            @Override
            protected LiveData<ApiResponse<RemoteLight>> loadFromNetwork() {
                return endpoint.getLight(dbObject.getEndpointLightId());
            }

            @Override
            protected UILight convertDbTypeToResultType(DbLight item) {
                return UILight.from(item);
            }

            @Override
            protected DbLight convertRemoteTypeToDbType(ApiSuccessResponse<RemoteLight> response) {
                return DbLight.from(response.getBody());
            }
        };
    }

    public LiveData<EmptyResource> setOnState(long lightId, boolean newState) {
        if (newState){
            LogUtil.i("Enable Light with Id %d", lightId);
        }else {
            LogUtil.i("Disable Light with Id %d", lightId);
        }

        return new NetworkUpdateResource<UILight, ResponseBody, DbLight>(true) {

            @Override
            protected LiveData<DbLight> loadFromDB() {
                return dbLightDao.load(lightId);
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
            protected LiveData<Resource<UILight>> loadUpdatedVersion() {
                LogUtil.v("Load updated light");
                return getLight(lightId);
            }
        };
    }

    public LiveData<EmptyResource> setBrightness(long lightId,  @IntRange(from = 0, to = 100) int brightness) {
        LogUtil.i("Set brightness to %d %% for light with id %d", brightness, lightId);
        if (brightness < 0 || brightness > 100) {
            throw new IllegalStateException(
                "The new brightness for light " + lightId + " has to be between 0 and 100 and not " + brightness);
        }

        return new NetworkUpdateResource<UILight, ResponseBody, DbLight>() {

            @Override
            protected LiveData<DbLight> loadFromDB() {
                return dbLightDao.load(lightId);
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
            protected LiveData<Resource<UILight>> loadUpdatedVersion() {
                LogUtil.v("Load updated light");
                return getLight(lightId);
            }
        };
    }
}