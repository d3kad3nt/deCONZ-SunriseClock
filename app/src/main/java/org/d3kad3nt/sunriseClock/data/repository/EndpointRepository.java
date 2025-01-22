package org.d3kad3nt.sunriseClock.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.d3kad3nt.sunriseClock.data.local.AppDatabase;
import org.d3kad3nt.sunriseClock.data.local.EndpointConfigDao;
import org.d3kad3nt.sunriseClock.data.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointConfig;
import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointType;
import org.d3kad3nt.sunriseClock.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.data.model.endpoint.UIEndpoint;
import org.d3kad3nt.sunriseClock.data.remote.common.EndpointBuilder;
import org.d3kad3nt.sunriseClock.serviceLocator.ExecutorType;
import org.d3kad3nt.sunriseClock.serviceLocator.ServiceLocator;
import org.d3kad3nt.sunriseClock.util.LiveDataUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EndpointRepository {

    private static final Map<Long, LiveData<BaseEndpoint>> endpointLiveDataCache = new HashMap<>();
    private static EndpointConfigDao endpointConfigDao;
    private static volatile EndpointRepository INSTANCE;

    private EndpointRepository(Context context) {
        endpointConfigDao = AppDatabase.getInstance(context.getApplicationContext()).endpointConfigDao();
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
                return null;
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

    private BaseEndpoint createEndpoint(EndpointConfig config) {
        if (config == null) {
            throw new NullPointerException("The given config object was null.");
        }
        EndpointBuilder builder = config.type.getBuilder();
        return builder.setConfig(config).build();
    }

    public IEndpointUI createEndpoint(Map<String, String> config) {
        if (config == null) {
            throw new NullPointerException("The given config map was null.");
        }
        EndpointType type = EndpointType.valueOf(config.remove("type"));
        String name = config.remove("name");
        Gson gson = new Gson();
        JsonObject jsonConfig = gson.toJsonTree(config).getAsJsonObject();
        EndpointConfig endpointConfig = new EndpointConfig(type, name, new Date(), jsonConfig);
        ServiceLocator.getExecutor(ExecutorType.IO).execute(() -> {
            endpointConfigDao.save(endpointConfig);
        });
        type.getBuilder().setConfig(endpointConfig).build();
        return UIEndpoint.from(endpointConfig);
    }

    public void deleteEndpoint(long endpoint) {
        LiveDataUtil.observeOnce(endpointConfigDao.load(endpoint), endpointConfigDao::delete);
    }
}
