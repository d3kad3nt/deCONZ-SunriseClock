package org.d3kad3nt.sunriseClock.data.repository;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import org.d3kad3nt.sunriseClock.data.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiEmptyResponse;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiErrorResponse;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiResponse;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiSuccessResponse;
import org.d3kad3nt.sunriseClock.serviceLocator.ExecutorType;
import org.d3kad3nt.sunriseClock.serviceLocator.ServiceLocator;
import org.d3kad3nt.sunriseclock.util.ExtendedMediatorLiveData;

// Copied from the official Google architecture-components github-sample
// (https://github.com/android/architecture-components-samples/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/repository/NetworkBoundResource.kt).

/** A generic class that can provide a resource backed by both the sqlite database and the network. */
public abstract class NetworkBoundResource<ResultType, RemoteType, DbType>
        extends ExtendedMediatorLiveData<Resource<ResultType>> {

    private final LiveData<DbType> dbSource;
    protected BaseEndpoint endpoint = null;
    protected DbType dbObject = null;

    public NetworkBoundResource() {
        this.setValue(Resource.loading(null));

        dbSource = loadFromDb();
        this.addSource(dbSource, dbObject -> {
            dbSourceObserver(dbObject, dbSource);
        });
    }

    private void dbSourceObserver(DbType data, LiveData<DbType> dbSourceLiveData) {
        if (data == null) {
            updateValue(Resource.loading(null));
        } else {
            dbObject = data;
            removeSource(dbSourceLiveData);
            if (shouldFetch(dbObject)) {
                LiveData<BaseEndpoint> endpointLiveData = loadEndpoint();
                addSource(endpointLiveData, baseEndpoint -> {
                    endpointLiveDataObserver(baseEndpoint, endpointLiveData);
                });
            } else {
                addSource(dbSource, newData -> {
                    updateValue(Resource.success(convertDbTypeToResultType(newData)));
                });
            }
        }
    }

    private void endpointLiveDataObserver(BaseEndpoint endpoint, LiveData<BaseEndpoint> endpointLiveData) {
        if (endpoint == null) {
            updateValue(Resource.loading(null));
        } else {
            this.endpoint = endpoint;
            fetchFromNetwork(dbSource);
            removeSource(endpointLiveData);
        }
    }

    protected void fetchFromNetwork(LiveData<DbType> dbSource) {
        LiveData<ApiResponse<RemoteType>> apiResponse = loadFromNetwork();
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        addSource(dbSource, newData -> {
            updateValue(Resource.loading(convertDbTypeToResultType(newData)));
        });
        addSource(apiResponse, response -> {
            removeSource(apiResponse);
            removeSource(dbSource);
            Class<? extends ApiResponse> aClass = response.getClass();
            if (ApiSuccessResponse.class.equals(aClass)) {
                ServiceLocator.getExecutor(ExecutorType.IO).execute(() -> {
                    saveResponseToDb(convertRemoteTypeToDbType((ApiSuccessResponse<RemoteType>) response));
                    ServiceLocator.getExecutor(ExecutorType.MainThread).execute(() -> {
                        // we specially request a new live data,
                        // otherwise we will get immediately last cached value,
                        // which may not be updated with latest results received from
                        // network.
                        addSource(loadFromDb(), newData -> {
                            updateValue(Resource.success(convertDbTypeToResultType(newData)));
                        });
                    });
                });
            } else if (ApiEmptyResponse.class.equals(aClass)) {
                ServiceLocator.getExecutor(ExecutorType.MainThread).execute(() -> {
                    // reload from disk whatever we had
                    addSource(loadFromDb(), newData -> {
                        updateValue(Resource.success(convertDbTypeToResultType(newData)));
                    });
                });
            } else if (ApiErrorResponse.class.equals(aClass)) {
                onFetchFailed();
                addSource(dbSource, newData -> {
                    updateValue(Resource.error(
                            ((ApiErrorResponse<RemoteType>) response).getErrorMessage(),
                            convertDbTypeToResultType(newData)));
                });
            }
        });
    }

    protected void onFetchFailed() {}

    /**
     * Insert data into the database, for example by calling insert or update methods on Room's DAO classes:
     *
     * <p>{@code dbLightDao.upsert(item);}
     */
    @WorkerThread
    protected abstract void saveResponseToDb(DbType item);

    @MainThread
    protected abstract boolean shouldFetch(DbType data);

    @MainThread
    protected abstract LiveData<BaseEndpoint> loadEndpoint();

    @MainThread
    protected abstract LiveData<DbType> loadFromDb();

    /**
     * Load fresh data from the network, for example by calling methods on the endpoint (backed e.g. by Retrofit network
     * requests):
     *
     * <p>{@code return endpoint.getLight(dbObject.getEndpointEntityId());}
     */
    @MainThread
    protected abstract LiveData<ApiResponse<RemoteType>> loadFromNetwork();

    /**
     * Convert from database data transfer objects to the desired result type (e.g. for use in the GUI), for example:
     *
     * <p>{@code return UILight.from(item);}
     *
     * @param item The entity/data returned by the database.
     */
    protected abstract ResultType convertDbTypeToResultType(DbType item);

    /**
     * Convert from remote (network) data transfer objects to a object suitable for database inserts or updates. This is
     * given directly to {@link #saveResponseToDb(DbType)}. For example:
     *
     * <p>{@code return DbLight.from(response.getBody());}
     *
     * @param response The data returned by the remote (network).
     */
    protected abstract DbType convertRemoteTypeToDbType(ApiSuccessResponse<RemoteType> response);
}
