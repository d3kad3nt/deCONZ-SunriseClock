package org.d3kad3nt.sunriseClock.data.repository;

import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;
import org.d3kad3nt.sunriseClock.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiEmptyResponse;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiErrorResponse;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiResponse;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiSuccessResponse;
import org.d3kad3nt.sunriseClock.serviceLocator.ExecutorType;
import org.d3kad3nt.sunriseClock.serviceLocator.ServiceLocator;

/** A generic class that can provide a resource backed by both the sqlite database and TWO network operations. */
abstract class BiNetworkBoundResource<ResultType, RemoteType1, RemoteType2, DbType>
        extends NetworkBoundResource<ResultType, RemoteType1, DbType> {

    private ApiResponse<RemoteType1> data1;
    private ApiResponse<RemoteType2> data2;

    /** A generic class that can provide a resource backed by both the sqlite database and TWO network operations. */
    public BiNetworkBoundResource() {
        super();
    }

    @Override
    protected void fetchFromNetwork(LiveData<DbType> dbSource) {
        LiveData<ApiResponse<RemoteType1>> apiResponse1 = loadFromNetwork();
        LiveData<ApiResponse<RemoteType2>> apiResponse2 = loadFromNetwork2();
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        addSource(dbSource, newData -> {
            updateValue(Resource.loading(convertDbTypeToResultType(newData)));
        });
        addSource(apiResponse1, response -> {
            data1 = response;
            removeSource(apiResponse1);
            if (data2 != null) {
                combineNetworkResults(dbSource);
            }
        });
        addSource(apiResponse2, response -> {
            data2 = response;
            removeSource(apiResponse2);
            if (data1 != null) {
                combineNetworkResults(dbSource);
            }
        });
    }

    @Deprecated
    @Override
    protected DbType convertRemoteTypeToDbType(final ApiSuccessResponse<RemoteType1> response) {
        throw new UnsupportedOperationException("The method convertRemoteTypeToDbType() should not be called on "
                + "BiNetworkBoundResource because this method cannot handle multiple network responses as required by "
                + "BiNetworkBoundResource.");
    }

    private void combineNetworkResults(final LiveData<DbType> dbSource) {
        removeSource(dbSource);
        Class<? extends ApiResponse> aClass = data1.getClass();
        Class<? extends ApiResponse> bClass = data2.getClass();
        if (ApiSuccessResponse.class.equals(aClass) && ApiSuccessResponse.class.equals(bClass)) {
            ServiceLocator.getExecutor(ExecutorType.IO).execute(() -> {
                saveResponseToDb(convertRemoteTypeToDbType(
                        (ApiSuccessResponse<RemoteType1>) data1, (ApiSuccessResponse<RemoteType2>) data2));
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
        } else if (ApiErrorResponse.class.equals(aClass)) {
            onFetchFailed();
            addSource(dbSource, newData -> {
                updateValue(Resource.error(
                        ((ApiErrorResponse<RemoteType1>) data1).getErrorMessage(), convertDbTypeToResultType(newData)));
            });
        } else if (ApiErrorResponse.class.equals(bClass)) {
            onFetchFailed();
            addSource(dbSource, newData -> {
                updateValue(Resource.error(
                        ((ApiErrorResponse<RemoteType2>) data2).getErrorMessage(), convertDbTypeToResultType(newData)));
            });
        } else if (ApiEmptyResponse.class.equals(aClass) || ApiEmptyResponse.class.equals(bClass)) {
            throw new UnsupportedOperationException("Empty responses not implemented");
        }
    }

    /**
     * Load additional fresh data from the network, for example by calling methods on the endpoint (backed e.g. by
     * Retrofit network requests):
     *
     * <p>{@code return endpoint.getGroup(dbObject.getEndpointEntityId());}
     *
     * <p>This is the second remote request and triggered right after {@link #loadFromNetwork()}. The result of both
     * methods can be used to construct a unified database entity in
     * {@link #convertRemoteTypeToDbType(ApiSuccessResponse, ApiSuccessResponse)}.
     */
    @MainThread
    protected abstract LiveData<ApiResponse<RemoteType2>> loadFromNetwork2();

    /**
     * Convert from two remote (network) data transfer objects to a object suitable for database inserts or updates.
     * This 'combined data' is given directly to {@link #saveResponseToDb(DbType)}.
     *
     * @param response The response for the first network request, triggered in {@link #loadFromNetwork()}.
     * @param response2 The response for the second network request, triggered in {@link #loadFromNetwork2()}.
     */
    protected abstract DbType convertRemoteTypeToDbType(
            ApiSuccessResponse<RemoteType1> response, final ApiSuccessResponse<RemoteType2> response2);
}
