package org.d3kad3nt.sunriseClock.backend.data.repository;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.d3kad3nt.sunriseClock.backend.data.local.AppDatabase;
import org.d3kad3nt.sunriseClock.backend.data.local.EndpointConfigDao;
import org.d3kad3nt.sunriseClock.backend.data.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.backend.data.model.endpoint.EndpointConfig;
import org.d3kad3nt.sunriseClock.backend.data.model.endpoint.EndpointType;
import org.d3kad3nt.sunriseClock.backend.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.backend.data.model.endpoint.UIEndpoint;
import org.d3kad3nt.sunriseClock.backend.data.remote.common.EndpointBuilder;
import org.d3kad3nt.sunriseClock.util.LogUtil;
import org.d3kad3nt.sunriseClock.util.serviceLocator.ExecutorType;
import org.d3kad3nt.sunriseClock.util.serviceLocator.ServiceLocator;

public final class EndpointRepository {

    private static final Map<Long, LiveData<BaseEndpoint>> endpointLiveDataCache = new HashMap<>();
    private static EndpointConfigDao endpointConfigDao;
    private static volatile EndpointRepository INSTANCE;

    private EndpointRepository(Context context) {
        endpointConfigDao =
                AppDatabase.getInstance(context.getApplicationContext()).endpointConfigDao();
    }

    public static EndpointRepository getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LightRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new EndpointRepository(context);
                }
            }
        }
        return INSTANCE;
    }

    LiveData<BaseEndpoint> getRepoEndpoint(long id) {
        if (!endpointLiveDataCache.containsKey(id)) {
            LiveData<BaseEndpoint> endpointTransformation =
                    Transformations.switchMap(endpointConfigDao.load(id), input -> {
                        if (input == null) {
                            return new MutableLiveData<>();
                        } else {
                            return new MutableLiveData<>(createEndpoint(input));
                        }
                    });
            endpointLiveDataCache.put(id, endpointTransformation);
        }
        return endpointLiveDataCache.get(id);
    }

    public LiveData<IEndpointUI> getEndpoint(long id) {
        return Transformations.map(endpointConfigDao.load(id), endpointConfig -> {
            if (endpointConfig == null) {
                throw new IllegalArgumentException("Invalid endpoint id: " + id);
            } else {
                return UIEndpoint.from(endpointConfig);
            }
        });
    }

    public LiveData<List<IEndpointUI>> getAllEndpoints() {
        return Transformations.switchMap(endpointConfigDao.loadAll(), input -> {
            if (input == null) {
                return new MutableLiveData<>(Collections.emptyList());
            } else {
                List<IEndpointUI> list = new ArrayList<>();
                for (EndpointConfig config : input) {
                    list.add(UIEndpoint.from(config));
                }
                return new MutableLiveData<>(list);
            }
        });
    }

    private BaseEndpoint createEndpoint(@NonNull EndpointConfig config) {
        EndpointBuilder builder = config.type.getBuilder();
        return builder.setConfig(config).build();
    }

    /** @noinspection UnusedReturnValue */
    @NonNull
    public IEndpointUI createEndpoint(@NonNull Map<String, String> config) {
        EndpointType type = EndpointType.valueOf(config.remove("type"));
        String name = config.remove("name");
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("A name has to be provided");
        }
        Gson gson = new Gson();
        JsonObject jsonConfig = gson.toJsonTree(config).getAsJsonObject();
        EndpointConfig endpointConfig = new EndpointConfig(type, name, new Date(), jsonConfig);
        ServiceLocator.getExecutor(ExecutorType.IO).execute(() -> {
            endpointConfigDao.save(endpointConfig);
        });
        // This is done to verify that the endpoint is created correctly
        type.getBuilder().setConfig(endpointConfig).build();
        return UIEndpoint.from(endpointConfig);
    }

    public void setName(long endpointId, String newName) {
        LogUtil.i("Setting name to %s for endpoint with id %d", newName, endpointId);

        EndpointConfigDao.NameUpdate update = new EndpointConfigDao.NameUpdate();
        update.endpointId = endpointId;
        update.name = newName;

        ServiceLocator.getExecutor(ExecutorType.IO).execute(() -> {
            endpointConfigDao.updateName(update);
        });
    }

    public void deleteEndpoint(long endpointId) {
        LogUtil.i("Deleting endpoint with id %d", endpointId);
        ServiceLocator.getExecutor(ExecutorType.IO).execute(() -> {
            endpointConfigDao.delete(endpointId);
        });
    }
}
