/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
 */

package org.d3kad3nt.sunriseClock.backend.data.model.resource;

/**
 * Status of a resource that is provided to the UI. Copied from the official Google architecture-components
 * github-sample under https://github.com/android/architecture-components-samples/blob/master/GithubBrowserSample
 * /app/src/main/java/com/android/example/github/vo/Status.kt
 *
 * <p>These are usually created by the Repository classes where they return 'LiveData<Resource<T>>' to pass back the
 * latest data to the UI with its fetch status.
 */
public enum Status {
    SUCCESS,
    ERROR,
    LOADING
}
