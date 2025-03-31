package org.d3kad3nt.sunriseClock.backend.data.model.resource;

/**
 * A generic class that holds a value with its loading status. Copied from the official Google architecture-components
 * github-sample under https://github.com/android/architecture-components-samples/blob/master/GithubBrowserSample
 * /app/src/main/java/com/android/example/github/vo/Resource.kt
 *
 * @param <T>
 */
public class Resource<T> {

    private final Status status;
    private final T data;
    private final String message;

    public Resource(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg, T data) {
        return new Resource<>(Status.ERROR, data, msg);
    }

    public static <T> Resource<T> loading(T data) {
        return new Resource<>(Status.LOADING, data, null);
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
