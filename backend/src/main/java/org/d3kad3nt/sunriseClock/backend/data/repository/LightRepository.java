package org.d3kad3nt.sunriseClock.backend.data.repository;

import android.content.Context;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import okhttp3.ResponseBody;
import org.d3kad3nt.sunriseClock.backend.data.local.AppDatabase;
import org.d3kad3nt.sunriseClock.backend.data.local.DbGroupDao;
import org.d3kad3nt.sunriseClock.backend.data.local.DbGroupLightCrossref;
import org.d3kad3nt.sunriseClock.backend.data.local.DbLightDao;
import org.d3kad3nt.sunriseClock.backend.data.local.DbLightGroupingDao;
import org.d3kad3nt.sunriseClock.backend.data.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.backend.data.model.group.DbGroup;
import org.d3kad3nt.sunriseClock.backend.data.model.group.RemoteGroup;
import org.d3kad3nt.sunriseClock.backend.data.model.group.UIGroup;
import org.d3kad3nt.sunriseClock.backend.data.model.light.DbLight;
import org.d3kad3nt.sunriseClock.backend.data.model.light.RemoteLight;
import org.d3kad3nt.sunriseClock.backend.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.EmptyResource;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.backend.data.remote.common.ApiResponse;
import org.d3kad3nt.sunriseClock.backend.data.remote.common.ApiSuccessResponse;
import org.d3kad3nt.sunriseClock.util.Empty;
import org.d3kad3nt.sunriseClock.util.LogUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Repository module for handling data operations (network or local database). */
public class LightRepository {

    private static DbLightDao dbLightDao;
    private static DbGroupDao dbGroupDao;
    private static DbLightGroupingDao dbLightGroupingDao;
    private static volatile LightRepository INSTANCE;
    private final EndpointRepository endpointRepo;

    /**
     * Using singleton pattern as of now. With dependency injection (Dagger, ...) this class could be mocked when unit
     * testing. TODO: Dependency Injection, optional
     */
    private LightRepository(Context context) {
        dbLightDao = AppDatabase.getInstance(context.getApplicationContext()).dbLightDao();
        dbGroupDao = AppDatabase.getInstance(context.getApplicationContext()).dbGroupDao();
        dbLightGroupingDao =
                AppDatabase.getInstance(context.getApplicationContext()).dbGroupLightCrossrefDao();
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
        LogUtil.i("Requesting and returning all lights for endpoint with id %d", endpointId);

        try {
            endpointRepo.getEndpoint(endpointId);
        } catch (NullPointerException e) {
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
                // TODO
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
                return dbLightDao.loadAllForEndpoint(endpointId);
            }

            @NotNull
            @Override
            protected LiveData<ApiResponse<List<RemoteLight>>> loadFromNetwork() {
                return endpoint.getLights();
            }

            @Override
            protected List<UILight> convertDbTypeToResultType(List<DbLight> items) {
                return UILight.from(items);
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

    public LiveData<EmptyResource> refreshLightsForEndpoint(long endpointId) {
        LogUtil.i("Refreshing all lights for endpoint with id %d", endpointId);

        return Transformations.map(
                new NetworkBoundResource<Empty, List<RemoteLight>, List<DbLight>>() {

                    @Override
                    protected void saveResponseToDb(List<DbLight> items) {
                        for (DbLight light : items) {
                            dbLightDao.upsert(light);
                        }
                    }

                    @Override
                    protected boolean shouldFetch(@Nullable List<DbLight> data) {
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
                    protected Empty convertDbTypeToResultType(List<DbLight> items) {
                        return new Empty();
                    }

                    @Override
                    protected List<DbLight> convertRemoteTypeToDbType(ApiSuccessResponse<List<RemoteLight>> response) {
                        List<DbLight> lights = new ArrayList<>();
                        for (RemoteLight light : response.getBody()) {
                            lights.add(DbLight.from(light));
                        }
                        return lights;
                    }
                },
                emptyResource -> EmptyResource.fromResource(emptyResource));
    }

    public LiveData<Resource<UILight>> getLight(long lightId) {
        LogUtil.i("Requesting and returning single light with id %d", lightId);

        return new NetworkBoundResource<UILight, RemoteLight, DbLight>() {

            @Override
            protected void saveResponseToDb(DbLight item) {
                // The primary key lightId is not known to the remote endpoint, but it is known to
                // us.
                // Set the lightId to enable direct update/insert via primary key (instead of
                // endpointId and
                // endpointLightId) through Room.
                item.setId(lightId);
                dbLightDao.upsert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable DbLight data) {
                // TODO
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
                return endpoint.getLight(dbObject.getEndpointEntityId());
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

    public LiveData<EmptyResource> refreshLight(long lightId) {
        LogUtil.i("Refreshing single light with id %d", lightId);

        return Transformations.map(
                new NetworkBoundResource<Empty, RemoteLight, DbLight>() {

                    @Override
                    protected void saveResponseToDb(DbLight item) {
                        // The primary key lightId is not known to the remote endpoint, but it is
                        // known to us.
                        // Set the lightId to enable direct update/insert via primary key (instead
                        // of endpointId and
                        // endpointLightId) through Room.
                        item.setId(lightId);
                        dbLightDao.upsert(item);
                    }

                    @Override
                    protected boolean shouldFetch(@Nullable DbLight data) {
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
                        return endpoint.getLight(dbObject.getEndpointEntityId());
                    }

                    @Override
                    protected Empty convertDbTypeToResultType(DbLight item) {
                        return new Empty();
                    }

                    @Override
                    protected DbLight convertRemoteTypeToDbType(ApiSuccessResponse<RemoteLight> response) {
                        return DbLight.from(response.getBody());
                    }
                },
                emptyResource -> EmptyResource.fromResource(emptyResource));
    }

    /**
     * Turns the light on or off.
     *
     * @param lightId The light to be turned on or off. The given ID must already exist in the database. Note that this
     *     ID is independent from the identifier that the backing endpoint uses internally.
     * @param newState Whether the light should be turned on (true) or off (false).
     * @return Resource representing the status of the request.
     */
    public LiveData<EmptyResource> setOnState(long lightId, boolean newState) {
        if (newState) {
            LogUtil.i("Enabling single light with id %d", lightId);
        } else {
            LogUtil.i("Disabling single light with id %d", lightId);
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
                return baseEndpoint.setOnState(dbObject.getEndpointEntityId(), newState);
            }

            @NotNull
            @Override
            protected LiveData<Resource<UILight>> loadUpdatedVersion() {
                LogUtil.d("Loading updated light with id %d after successful setOn change", lightId);
                return getLight(lightId);
            }
        };
    }

    /**
     * Toggle all lights from on to off or vice versa.
     *
     * <p>If one or more lights are currently turned on, those are turned off. If all lights are currently turned off,
     * all lights are turned on.
     *
     * @param endpointId The endpoint on which to execute the action. The given ID must already exist in the database.
     * @return Resource representing the status of the request.
     */
    public LiveData<EmptyResource> toggleOnStateForEndpoint(long endpointId) {

        return new NetworkUpdateResource<List<UILight>, ResponseBody, List<DbLight>>(true) {

            @Override
            protected LiveData<List<DbLight>> loadFromDB() {
                return dbLightDao.loadAllForEndpoint(endpointId);
            }

            @Override
            protected LiveData<BaseEndpoint> loadEndpoint() {
                return endpointRepo.getRepoEndpoint(endpointId);
            }

            @NonNull
            @Override
            protected @NotNull LiveData<ApiResponse<ResponseBody>> sendNetworkRequest(BaseEndpoint baseEndpoint) {
                return baseEndpoint.toggleOnState();
            }

            @Override
            protected @NotNull LiveData<Resource<List<UILight>>> loadUpdatedVersion() {
                return getLightsForEndpoint(endpointId);
            }
        };
    }

    /**
     * Changes the brightness of the light. TODO: Define whether a brightness of 0 means off or lowest brightness (but
     * still on).
     *
     * @param lightId The light that this action executes on. The given ID must already exist in the database. Note that
     *     this ID is independent from the identifier that the backing endpoint uses internally.
     * @param brightness Desired light brightness, ranging from 0 (lowest) to 100 (highest).
     * @return Resource representing the status of the request.
     */
    public LiveData<EmptyResource> setBrightness(long lightId, @IntRange(from = 0, to = 100) int brightness) {
        LogUtil.i("Setting brightness to %d %% for single light with id %d", brightness, lightId);
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
                return baseEndpoint.setBrightness(dbObject.getEndpointEntityId(), brightness);
            }

            @NotNull
            @Override
            protected LiveData<Resource<UILight>> loadUpdatedVersion() {
                LogUtil.d("Loading updated light with id %d after successful brightness change", lightId);
                return getLight(lightId);
            }
        };
    }

    public LiveData<EmptyResource> setName(long lightId, String newName) {
        LogUtil.i("Setting name to %s for single light with id %d", newName, lightId);

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
                return baseEndpoint.setName(dbObject.getEndpointEntityId(), newName);
            }

            @NotNull
            @Override
            protected LiveData<Resource<UILight>> loadUpdatedVersion() {
                LogUtil.d("Loading updated light with id %d after successful name change", lightId);
                return getLight(lightId);
            }
        };
    }

    public LiveData<Resource<Map<UIGroup, List<UILight>>>> getGroupsWithLightsForEndpoint(long endpointId) {
        try {
            endpointRepo.getEndpoint(endpointId);
        } catch (NullPointerException e) {
            Resource<Map<UIGroup, List<UILight>>> resource = Resource.error("Endpoint doesn't exist", null);
            return new MutableLiveData<>(resource);
        }
        return new BiNetworkBoundResource<
                Map<UIGroup, List<UILight>>, List<RemoteGroup>, List<RemoteLight>, Map<DbGroup, List<DbLight>>>() {

            @Override
            protected void saveResponseToDb(Map<DbGroup, List<DbLight>> items) {
                items.forEach((dbGroup, dbLights) -> {
                    long dbGroupPk = dbGroupDao.upsert(dbGroup);
                    dbLights.forEach(dbLight -> {
                        long dbLightPk = dbLightDao.upsert(dbLight);
                        dbLightGroupingDao.save(new DbGroupLightCrossref(dbGroupPk, dbLightPk));
                    });
                });
            }

            @Override
            protected boolean shouldFetch(Map<DbGroup, List<DbLight>> data) {
                // TODO
                return true;
            }

            @Override
            protected LiveData<BaseEndpoint> loadEndpoint() {
                return endpointRepo.getRepoEndpoint(endpointId);
            }

            @Override
            protected LiveData<Map<DbGroup, List<DbLight>>> loadFromDb() {
                return dbGroupDao.loadGroupsWithLightsForEndpoint(endpointId);
            }

            @Override
            protected LiveData<ApiResponse<List<RemoteGroup>>> loadFromNetwork() {
                return endpoint.getGroups();
            }

            @Override
            protected LiveData<ApiResponse<List<RemoteLight>>> loadFromNetwork2() {
                return endpoint.getLights();
            }

            @Override
            protected Map<UIGroup, List<UILight>> convertDbTypeToResultType(
                    Map<DbGroup, List<DbLight>> groupsWithLights) {
                return groupsWithLights.entrySet().stream()
                        .collect(Collectors.toMap(
                                groupWithLights -> UIGroup.from(groupWithLights.getKey()),
                                groupWithLights -> UILight.from(groupWithLights.getValue())));
            }

            @Override
            protected Map<DbGroup, List<DbLight>> convertRemoteTypeToDbType(
                    ApiSuccessResponse<List<RemoteGroup>> remoteGroups,
                    ApiSuccessResponse<List<RemoteLight>> response2) {
                List<DbLight> lights = new ArrayList<>();
                Map<DbGroup, List<DbLight>> groupsWithLights = new HashMap<>();

                for (RemoteLight light : response2.getBody()) {
                    lights.add(DbLight.from(light));
                }

                for (RemoteGroup group : remoteGroups.getBody()) {
                    DbGroup dbGroup = DbGroup.from(group);
                    List<DbLight> groupLights = lights.stream()
                            .filter(dbLight -> group.getEndpointLightIds().contains(dbLight.getEndpointEntityId()))
                            .toList();
                    groupsWithLights.put(dbGroup, groupLights);
                }
                return groupsWithLights;
            }
        };
    }

    public LiveData<Resource<List<UIGroup>>> getGroupsForEndpoint(long endpointId) {
        LogUtil.i("Requesting and returning all groups for endpoint with id %d", endpointId);

        try {
            endpointRepo.getEndpoint(endpointId);
        } catch (NullPointerException e) {
            Resource<List<UIGroup>> resource = Resource.error("Endpoint doesn't exist", null);
            return new MutableLiveData<>(resource);
        }
        return new NetworkBoundResource<List<UIGroup>, List<RemoteGroup>, List<DbGroup>>() {

            @Override
            protected void saveResponseToDb(List<DbGroup> items) {
                for (DbGroup group : items) {
                    // Todo: Work with @Ignore to extract lightIds from object and manually insert
                    // them into the
                    // crossref table. Is this still necessary since getGroupsWithLightsForEndpoint?
                    dbGroupDao.upsert(group);
                }
            }

            @Override
            protected boolean shouldFetch(List<DbGroup> data) {
                // TODO
                return true;
            }

            @Override
            protected LiveData<BaseEndpoint> loadEndpoint() {
                return endpointRepo.getRepoEndpoint(endpointId);
            }

            @Override
            protected LiveData<List<DbGroup>> loadFromDb() {
                return dbGroupDao.loadAllForEndpoint(endpointId);
            }

            @Override
            protected LiveData<ApiResponse<List<RemoteGroup>>> loadFromNetwork() {
                return endpoint.getGroups();
            }

            @Override
            protected List<UIGroup> convertDbTypeToResultType(List<DbGroup> dbGroups) {
                return UIGroup.from(dbGroups);
            }

            @Override
            protected List<DbGroup> convertRemoteTypeToDbType(ApiSuccessResponse<List<RemoteGroup>> response) {
                List<DbGroup> groups = new ArrayList<>();
                for (RemoteGroup group : response.getBody()) {
                    groups.add(DbGroup.from(group));
                }
                return groups;
            }
        };
    }

    public LiveData<EmptyResource> refreshGroupsForEndpoint(long endpointId) {
        LogUtil.i("Refreshing all groups for endpoint with id %d", endpointId);

        return Transformations.map(
            new NetworkBoundResource<Empty, List<RemoteGroup>, List<DbGroup>>() {

                @Override
                protected void saveResponseToDb(List<DbGroup> items) {
                    for (DbGroup group : items) {
                        dbGroupDao.upsert(group);
                    }
                }

                @Override
                protected boolean shouldFetch(@Nullable List<DbGroup> data) {
                    return true;
                }

                @NonNull
                @Override
                protected LiveData<BaseEndpoint> loadEndpoint() {
                    return endpointRepo.getRepoEndpoint(endpointId);
                }

                @NotNull
                @Override
                protected LiveData<List<DbGroup>> loadFromDb() {
                    return Transformations.map(dbGroupDao.loadAllForEndpoint(endpointId), input -> {
                        return new ArrayList<>(input);
                    });
                }

                @NotNull
                @Override
                protected LiveData<ApiResponse<List<RemoteGroup>>> loadFromNetwork() {
                    return endpoint.getGroups();
                }

                @Override
                protected Empty convertDbTypeToResultType(List<DbGroup> items) {
                    return new Empty();
                }

                @Override
                protected List<DbGroup> convertRemoteTypeToDbType(ApiSuccessResponse<List<RemoteGroup>> response) {
                    List<DbGroup> groups = new ArrayList<>();
                    for (RemoteGroup group : response.getBody()) {
                        groups.add(DbGroup.from(group));
                    }
                    return groups;
                }
            },
            emptyResource -> EmptyResource.fromResource(emptyResource));
    }
}
