package org.d3kad3nt.sunriseClock.data.remote.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.d3kad3nt.sunriseClock.util.Empty;

public class EmptyResource extends Resource<Empty> {
    protected EmptyResource(@NonNull Status status, @Nullable String message) {
        super(status, Empty.getInstance(), message);
    }

    static EmptyResource success(@Nullable String message){
        return new EmptyResource(Status.SUCCESS, message);
    }

    static EmptyResource error(String message){
        return new EmptyResource(Status.ERROR, message);
    }

    static EmptyResource loading(String message){
        return new EmptyResource(Status.LOADING, message);
    }

    public static <T> EmptyResource fromResource(Resource<T> otherResource) {
        return new EmptyResource(otherResource.getStatus(), otherResource.getMessage());
    }
}
