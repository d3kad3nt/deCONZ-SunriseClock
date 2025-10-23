package org.d3kad3nt.sunriseClock.backend.data.model.resource;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.d3kad3nt.sunriseClock.util.Empty;

/**
 * A specialized {@link Resource} class that holds no data.
 *
 * <p>This class is useful for representing the state of an operation (e.g. success, error, loading) where there is no
 * specific data payload to return, only a status and an optional message.
 */
public class EmptyResource extends Resource<Empty> {

    protected EmptyResource(@NonNull Status status, @Nullable String message) {
        super(status, Empty.getInstance(), message);
    }

    public static EmptyResource success(@Nullable String message) {
        return new EmptyResource(Status.SUCCESS, message);
    }

    public static EmptyResource error(String message) {
        return new EmptyResource(Status.ERROR, message);
    }

    public static EmptyResource loading(String message) {
        return new EmptyResource(Status.LOADING, message);
    }

    public static <T> EmptyResource fromResource(Resource<T> otherResource) {
        return new EmptyResource(otherResource.getStatus(), otherResource.getMessage());
    }
}
