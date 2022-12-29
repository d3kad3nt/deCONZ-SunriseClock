package org.d3kad3nt.sunriseClock.util;

import androidx.annotation.NonNull;

//TODO: Delete unused settings keys as testConnection method was deleted
public enum SettingKeys {
    ACTIVATED_LIGHTS("activatedLights"),        //StringSet
    ALARM_ACTIVE("pref_alarm_active"),          //Boolean
    ALARM_TIME("next_alarm_time"),              //Long
    API_KEY("pref_api_key"),                    //String
    IP("pref_ip"),                              //String
    PORT("pref_port"),                          //String
    TOAST_ACTIVE("pref_schedule_set_toast"),    //Boolean
    TEST_CONNECTION("pref_test_connection"),    //String
    ;

    String name;

    SettingKeys(String string) {
        name = string;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
