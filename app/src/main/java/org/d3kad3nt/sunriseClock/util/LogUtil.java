package org.d3kad3nt.sunriseClock.util;

import android.util.Log;

public class LogUtil {
    public static void v(String message){
        Log.v(getCallerClassName(), message);
    }

    public static void v(String message, String ... parameters){
        v(String.format(message, (Object[]) parameters));
    }

    public static void d(String message){
        Log.d(getCallerClassName(), message);
    }

    public static void d(String message, String ... parameters){
        d(String.format(message, (Object[]) parameters));
    }

    public static void i(String message){
        Log.i(getCallerClassName(), message);
    }

    public static void i(String message, String ... parameters){
        i(String.format(message, (Object[]) parameters));
    }

    public static void w(String message){
        Log.w(getCallerClassName(), message);
    }

    public static void w(String message, String ... parameters){
        w(String.format(message, (Object[]) parameters));
    }

    public static void e(String message){
        Log.e(getCallerClassName(), message);
    }

    public static void e(String message, String ... parameters){
        e(String.format(message, (Object[]) parameters));
    }

    private static String getCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i=1; i<stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(LogUtil.class.getName()) && ste.getClassName().indexOf("java.lang.Thread")!=0) {
                String[] splitClassName = ste.getClassName().split("\\.");
                return splitClassName[splitClassName.length-1];
            }
        }
        return "No Class Found";
    }
}
