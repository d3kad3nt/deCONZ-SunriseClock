package org.d3kad3nt.sunriseClock.util;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class LogUtil {

    private static final Map<String, String> prefixCache = new HashMap<>();

    public static void v(String message){
        String callerClass = getCallerClassName();
        if (prefixCache.containsKey(callerClass)) {
            Log.v(getCallerClassSimpleName(), prefixCache.get(callerClass) + message);
        } else {
            Log.v(getCallerClassSimpleName(), message);
        }
    }

    public static void v(String message, Object ... parameters){
        v(String.format(message, parameters));
    }

    public static void d(String message){
        String callerClass = getCallerClassName();
        if (prefixCache.containsKey(callerClass)) {
            Log.d(getCallerClassSimpleName(), prefixCache.get(callerClass) + message);
        } else {
            Log.d(getCallerClassSimpleName(), message);
        }
    }

    public static void d(String message, Object ... parameters){
        d(String.format(message, parameters));
    }

    public static void i(String message){
        String callerClass = getCallerClassName();
        if (prefixCache.containsKey(callerClass)) {
            Log.i(getCallerClassSimpleName(), prefixCache.get(callerClass) + message);
        } else {
            Log.i(getCallerClassSimpleName(), message);
        }
    }

    public static void i(String message, Object... parameters){
        i(String.format(message, parameters));
    }

    public static void w(String message){
        String callerClass = getCallerClassName();
        if (prefixCache.containsKey(callerClass)) {
            Log.w(getCallerClassSimpleName(), prefixCache.get(callerClass) + message);
        } else {
            Log.w(getCallerClassSimpleName(), message);
        }
    }

    public static void w(String message, Object... parameters){
        w(String.format(message, parameters));
    }

    public static void e(String message){
        String callerClass = getCallerClassName();
        if (prefixCache.containsKey(callerClass)) {
            Log.e(getCallerClassSimpleName(), prefixCache.get(callerClass) + message);
        } else {
            Log.e(getCallerClassSimpleName(), message);
        }
    }

    public static void e(String message, Object... parameters){
        e(String.format(message, parameters));
    }

    private static String getCallerClassSimpleName() {
        String[] splitClassName = getCallerClassName().split("\\.");
        return splitClassName[splitClassName.length - 1];
    }
    private static String getCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i=1; i<stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(LogUtil.class.getName()) && ste.getClassName().indexOf("java.lang.Thread")!=0) {
                return ste.getClassName();
            }
        }
        return "No Class Found";
    }

    public static void setPrefix(final String prefix) {
        prefixCache.put(getCallerClassName(), prefix);
    }

    public static void setPrefix(final String prefix, Object... parameters) {
        setPrefix(String.format(prefix, parameters));
    }
}
