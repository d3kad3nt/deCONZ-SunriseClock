package org.d3kad3nt.sunriseClock.data.remote.common;

import java.io.IOException;

import retrofit2.Response;

// This is a reimplementation in Java of the "ApiResponse" Class which was copied from
// the official Google architecture-components github-sample under
// https://github.com/android/architecture-components-samples/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/api/ApiResponse.kt

//TODO: to use this class the subclasses have to be moved to new files because they have to be public.

public abstract class JApiResponse<T> {

    public static <T> JApiResponse<T> create(Throwable error){
        if (error.getMessage() != null){
            return new JApiErrorResponse<>(error.getMessage());
        }else {
            return new JApiErrorResponse<>("unknown error");
        }
    }

    public static <T> JApiResponse<T> create(final Response<T> response){
        if (response.isSuccessful()){
            final T body = response.body();
            if ( body == null || response.code() == 204){
                return new JApiEmptyResponse<>();
            }else{
                return new JApiSuccessResponse<>(body);
            }
        }else{
            String msg = null;
            if (response.errorBody() != null){
                try {
                    msg = response.errorBody().string();
                } catch (IOException ignored) {}
            }
            final String errorMsg;
            if ((msg == null) || msg.isEmpty()){
                if (response.message().isEmpty())
                {
                    errorMsg = "unknown error";
                }else{
                    errorMsg = response.message();
                }
            }else {
                errorMsg = msg;
            }
            return new JApiErrorResponse<>(errorMsg);
        }

    }
}

class JApiEmptyResponse<T> extends JApiResponse<T>{}

class JApiErrorResponse<T> extends JApiResponse<T>{

    private final String errorMessage;

    public JApiErrorResponse(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

class JApiSuccessResponse<T> extends JApiResponse<T> {
    private final T body;

    public JApiSuccessResponse(T body) {
        this.body = body;
    }

    public T getBody() {
        return body;
    }
}
