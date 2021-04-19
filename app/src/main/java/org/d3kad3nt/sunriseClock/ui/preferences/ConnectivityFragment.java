package org.d3kad3nt.sunriseClock.ui.preferences;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.model.AppDatabase;
import org.d3kad3nt.sunriseClock.model.LightRepository;
import org.d3kad3nt.sunriseClock.model.endpoint.EndpointConfig;
import org.d3kad3nt.sunriseClock.model.endpoint.EndpointConfigDao;
import org.d3kad3nt.sunriseClock.model.endpoint.EndpointType;
import org.d3kad3nt.sunriseClock.model.endpoint.deconz.DeconzEndpoint;
import org.d3kad3nt.sunriseClock.model.endpoint.remoteApi.Resource;
import org.d3kad3nt.sunriseClock.model.endpoint.remoteApi.Status;
import org.d3kad3nt.sunriseClock.model.light.BaseLight;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.d3kad3nt.sunriseClock.utils.SettingKeys.API_KEY;
import static org.d3kad3nt.sunriseClock.utils.SettingKeys.IP;
import static org.d3kad3nt.sunriseClock.utils.SettingKeys.PORT;
import static org.d3kad3nt.sunriseClock.utils.SettingKeys.TEST_CONNECTION;

public class ConnectivityFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = "ConnectivityFragment";

    @Override
    public void onResume(){
        super.onResume();
        // Register the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey){
        Log.i("sunriseClock", "Settings");
        setPreferencesFromResource(R.xml.preferences_connectivity, rootKey);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
        Log.d(TAG, key + " changed.");
    }
}
