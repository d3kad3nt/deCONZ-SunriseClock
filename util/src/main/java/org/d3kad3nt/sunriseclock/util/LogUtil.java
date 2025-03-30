/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
 */

package org.d3kad3nt.sunriseclock.util;

import android.util.Log;
import androidx.annotation.NonNull;
import java.util.HashMap;
import java.util.Map;

public class LogUtil {

    private static final Map<String, String> prefixCache = new HashMap<>();

    public static void v(String message) {
        String callerClass = getCallerClassName();
        if (prefixCache.containsKey(callerClass)) {
            Log.v(getSimpleClassName(callerClass), prefixCache.get(callerClass) + message);
        } else {
            Log.v(getSimpleClassName(callerClass), message);
        }
    }

    public static void v(String message, Object... parameters) {
        v(String.format(message, parameters));
    }

    public static void d(String message) {
        String callerClass = getCallerClassName();
        if (prefixCache.containsKey(callerClass)) {
            Log.d(getSimpleClassName(callerClass), prefixCache.get(callerClass) + message);
        } else {
            Log.d(getSimpleClassName(callerClass), message);
        }
    }

    public static void d(String message, Object... parameters) {
        d(String.format(message, parameters));
    }

    public static void i(String message) {
        String callerClass = getCallerClassName();
        if (prefixCache.containsKey(callerClass)) {
            Log.i(getSimpleClassName(callerClass), prefixCache.get(callerClass) + message);
        } else {
            Log.i(getSimpleClassName(callerClass), message);
        }
    }

    public static void i(String message, Object... parameters) {
        i(String.format(message, parameters));
    }

    public static void w(String message) {
        String callerClass = getCallerClassName();
        if (prefixCache.containsKey(callerClass)) {
            Log.w(getSimpleClassName(callerClass), prefixCache.get(callerClass) + message);
        } else {
            Log.w(getSimpleClassName(callerClass), message);
        }
    }

    public static void w(String message, Object... parameters) {
        w(String.format(message, parameters));
    }

    public static void e(String message) {
        String callerClass = getCallerClassName();
        if (prefixCache.containsKey(callerClass)) {
            Log.e(getSimpleClassName(callerClass), prefixCache.get(callerClass) + message);
        } else {
            Log.e(getSimpleClassName(callerClass), message);
        }
    }

    public static void e(String message, Object... parameters) {
        e(String.format(message, parameters));
    }

    private static String getSimpleClassName(@NonNull String className) {
        String[] splitClassName = className.split("\\.");
        return splitClassName[splitClassName.length - 1];
    }

    private static String getCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i = 1; i < stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(LogUtil.class.getName())
                    && ste.getClassName().indexOf("java.lang.Thread") != 0) {
                return ste.getClassName();
            }
        }
        return "No Class Found";
    }

    /** Remove the logging prefix for the current class (if one exists) */
    public static void removePrefix() {
        prefixCache.remove(getCallerClassName());
    }

    /**
     * Add a prefix before every Log Message that is created by this class until the prefix is changed or removed.
     *
     * <p>Info: The Prefix is class specific. This means, that different instances of the class use the same prefix
     *
     * @param prefix The Prefix that should be added
     */
    public static void setPrefix(final @NonNull String prefix) {
        prefixCache.put(getCallerClassName(), prefix);
    }

    /**
     * Add a prefix before every Log Message that is created by this class until the prefix is changed or removed.
     *
     * <p>Info: The Prefix is class specific. This means, that different instances of the class use the same prefix
     *
     * @param prefix The Prefix that should be added with String.format specifiers
     * @param parameters The Parameters for String.format
     */
    public static void setPrefix(final @NonNull String prefix, Object... parameters) {
        setPrefix(String.format(prefix, parameters));
    }
}
