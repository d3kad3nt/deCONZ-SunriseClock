package org.d3kad3nt.sunriseClock.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import org.d3kad3nt.sunriseClock.data.local.AppDatabase;
import org.d3kad3nt.sunriseClock.data.local.DbLightDao;
import org.d3kad3nt.sunriseClock.data.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.data.model.light.DbLight;
import org.d3kad3nt.sunriseClock.data.model.light.DbLightBuilder;
import org.d3kad3nt.sunriseClock.data.model.light.RemoteLight;
import org.d3kad3nt.sunriseClock.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.data.model.resource.EmptyResource;
import org.d3kad3nt.sunriseClock.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiResponse;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiSuccessResponse;
import org.d3kad3nt.sunriseClock.serviceLocator.ExecutorType;
import org.d3kad3nt.sunriseClock.serviceLocator.ServiceLocator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

/**
 * Repository module for handling data operations (network or local database).
 */
public class LightRepository {

    private final static String TAG = "LightRepository";
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

            @Override
            protected void onFetchFailed() {
                //Set Light State to not reachable
                LiveData<List<DbLight>> light = loadFromDb();
                light.observeForever(new Observer<List<DbLight>>() {
                    @Override
                    public void onChanged(final List<DbLight> dbLights) {
                        if (dbLights == null) {
                            return;
                        }
                        light.removeObserver(this);
                        ServiceLocator.getExecutor(ExecutorType.IO).execute(() -> {
                            for (DbLight light : dbLights) {
                                DbLight updatedLight = DbLightBuilder.from(light).setIsReachable(false).build();
                                dbLightDao.upsert(updatedLight);
                            }
                        });
                    }
                });
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
            protected void onFetchFailed() {
                //Set Light State to not reachable
                LiveData<DbLight> light = loadFromDb();
                light.observeForever(new Observer<DbLight>() {
                    @Override
                    public void onChanged(final DbLight dbLight) {
                        if (dbLight == null) {
                            return;
                        }
                        light.removeObserver(this);
                        ServiceLocator.getExecutor(ExecutorType.IO).execute(() -> {
                            DbLight updatedLight = DbLightBuilder.from(dbLight).setIsReachable(false).build();
                            dbLightDao.upsert(updatedLight);
                        });
                    }
                });
            }

            @Override
            protected DbLight convertRemoteTypeToDbType(ApiSuccessResponse<RemoteLight> response) {
                Log.d(TAG, "Convert: Light " + response.getBody().getName() + " is reachable: " +
                    response.getBody().getIsReachable());
                return DbLight.from(response.getBody());
            }
        };
    }

    public LiveData<EmptyResource> setOnState(long lightId, boolean newState) {

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
                return baseEndpoint.setOnState(dbObject.getEndpointLightId(), newState);
            }

            @NotNull
            @Override
            protected LiveData<Resource<UILight>> loadUpdatedVersion() {
                return getLight(lightId);
            }
        };
    }

    public LiveData<EmptyResource> setBrightness(long lightId, double brightness) {

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
                return getLight(lightId);
            }
        };
    }
}