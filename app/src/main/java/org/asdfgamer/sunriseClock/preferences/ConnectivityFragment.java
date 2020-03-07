package org.asdfgamer.sunriseClock.preferences;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.asdfgamer.sunriseClock.R;
import org.asdfgamer.sunriseClock.model.AppDatabase;
import org.asdfgamer.sunriseClock.model.LightEndpoint;
import org.asdfgamer.sunriseClock.model.LightRepository;
import org.asdfgamer.sunriseClock.model.endpoint.EndpointConfig;
import org.asdfgamer.sunriseClock.model.endpoint.EndpointConfigDao;
import org.asdfgamer.sunriseClock.model.endpoint.EndpointType;
import org.asdfgamer.sunriseClock.model.endpoint.EndpointWithLights;
import org.asdfgamer.sunriseClock.model.endpoint.deconz.DeconzEndpoint;
import org.asdfgamer.sunriseClock.model.endpoint.remoteApi.ApiResponse;
import org.asdfgamer.sunriseClock.model.endpoint.remoteApi.Resource;
import org.asdfgamer.sunriseClock.model.light.BaseLight;
import org.asdfgamer.sunriseClock.model.light.BaseLightDao;
import org.asdfgamer.sunriseClock.model.light.Light;
import org.asdfgamer.sunriseClock.network.lights.DeconzRequestLightsHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.asdfgamer.sunriseClock.utils.SettingKeys.API_KEY;
import static org.asdfgamer.sunriseClock.utils.SettingKeys.IP;
import static org.asdfgamer.sunriseClock.utils.SettingKeys.PORT;
import static org.asdfgamer.sunriseClock.utils.SettingKeys.TEST_CONNECTION;

public class ConnectivityFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "ConnectivityFragment";

    @Override
    public void onResume() {
        super.onResume();
        // Register the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Log.i("sunriseClock", "Settings");
        setPreferencesFromResource(R.xml.preferences_connectivity, rootKey);

        findPreference("pref_test_connection").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.d(TAG, "Click on testConnection registered.");
                testConnection();
                return true;
            }
        });

        //Manually update the preferences summary on creation of this fragment.
        //This is used instead of a custom SimpleSummaryProvider. This method is easier for
        //updating the summary from different dialogs (eg. AlertDialog).
        updatePrefSummary(findPreference("pref_test_connection").getSharedPreferences(), TEST_CONNECTION.toString());
    }

    private void testConnection() {

        final SharedPreferences preferences = findPreference("pref_test_connection").getSharedPreferences();

        final Uri.Builder deconzUribuilder = new Uri.Builder();
        deconzUribuilder.scheme("http")
                .encodedAuthority(preferences.getString(IP.toString(), ""));

        //An endpoint with the correct ID has to be created before saving a BaseLight into the database.
        //Otherwise the foreign key constraint would fail.
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1988);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date date = cal.getTime();

        DeconzEndpoint deconzTest = new DeconzEndpoint(deconzUribuilder.build().toString(), Integer.parseInt(preferences.getString(PORT.toString(), "")), preferences.getString(API_KEY.toString(), ""));
        Gson gson = new Gson();

        LightRepository repo = new LightRepository(getContext());
        EndpointConfigDao endpointConfigDao = AppDatabase.getInstance(this.getContext()).endpointConfigDao();

        EndpointConfig endpointConfig = new EndpointConfig(1, EndpointType.DECONZ, date, new JsonParser().parse(gson.toJson(deconzTest, DeconzEndpoint.class)).getAsJsonObject());
        LightEndpoint lightEndpoint = repo.createEndpoint(endpointConfig);
        LiveData<ApiResponse<BaseLight>> tmp = lightEndpoint.getLight(1);

        LiveData<Resource<List<BaseLight>>> lights = repo.testGetLights(1);
        lights.observe(this, observedtest -> {
            Log.d(TAG, "Status: " + observedtest.getStatus().toString());
        });

        BaseLight baselight = new BaseLight(1, 1,"Test",true,true,true,true);
        BaseLightDao baseLightDao = AppDatabase.getInstance(this.getContext()).baseLightDao();
        baseLightDao.save(baselight);

        //Test: Retrieve an endpoint with a list of associated lights.
        LiveData<List<EndpointWithLights>> test = endpointConfigDao.getEndpointWithLights();
        test.observe(this, observedtest -> {
            Log.d(TAG, "One attribute updated of EndpointWithLights (or initial async query returned): " + observedtest.get(0).endpointConfig.id);
        });

        LiveData<Light> light = repo.getLight(1);
        light.observe(this, observedLight -> {
            Log.d(TAG,"One attribute updated of light name (or initial async query returned): " + String.valueOf(observedLight.getFriendlyName()));

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(this.getContext()));
            alertDialogBuilder.setTitle("Light Test")
                    .setMessage("Friendly Name of test light: " + light.getValue().getFriendlyName());
            final AlertDialog testAlert = alertDialogBuilder.create();
            testAlert.show();
        });

        //
        //OLD:
        //

        final Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(preferences.getString(IP.toString(), "") + ":" + preferences.getString(PORT.toString(), ""));

        /* 1st Step: Tests connection (and authentication!) to deconz by requesting an API endpoint which is key-protected.
         * 2nd step: Requests some information from deconz to show it to the user. */
        DeconzRequestLightsHelper deconzLights = new DeconzRequestLightsHelper(builder.build(), preferences.getString(API_KEY.toString(), ""));

//        deconzLights.getLights(new CustomGetLightsCallback() {
//            @Override
//            public void onSuccess(Response<List<Light>> response) {
//                DeconzRequestConfigHelper deconzConfig = new DeconzRequestConfigHelper(builder.build(), preferences.getString(API_KEY.toString(), ""));
//
//                deconzConfig.getConfig(new SimplifiedCallback<Config>() {
//                    @Override
//                    public void onSuccess(Response<Config> response) {
//                        Config config = response.body();
//
//                        //TODO: Use xml layout for this alertDialog.
//                        alertDialog.setMessage(getResources().getString(R.string.connection_test_success_apiversion) + ": " + Objects.requireNonNull(config).getApiversion()
//                                + System.getProperty("line.separator")
//                                + getResources().getString(R.string.connection_test_success_ipaddress) + ": " + config.getIpaddress());
//                        alertDialog.setTitle(getResources().getString(R.string.connection_test_success_title));
//
//                        SharedPreferences.Editor prefEditor = preferences.edit();
//                        Calendar calendar = Calendar.getInstance();
//                        SimpleDateFormat mdformat = new SimpleDateFormat("dd.MM.YY HH:mm", Locale.getDefault());
//                        String strDate = mdformat.format(calendar.getTime());
//                        prefEditor.putString("pref_test_connection", strDate);
//                        prefEditor.apply();
//                    }
//
//                    @Override
//                    public void onError() {
//                        //This should not fail because getLights returned successfully just a few moments ago.
//                    }
//                });
//            }
//
//            @Override
//            public void onForbidden(Error error) {
//                alertDialog.setMessage(getResources().getString(R.string.connection_test_error_wrongapikey));
//            }
//
//            @Override
//            public void onNetworkFailure(Call<List<Light>> call, Throwable throwable) {
//                alertDialog.setMessage(getResources().getString(R.string.connection_test_error_noconnection));
//            }
//
//            @Override
//            public void onInvalidResponseObject(Call<List<Light>> call, Throwable throwable) {
//                alertDialog.setMessage(getResources().getString(R.string.connection_test_error_nodeconz));
//            }
//
//            @Override
//            public void onInvalidErrorObject() {
//                alertDialog.setMessage(getResources().getString(R.string.connection_test_error_nodeconz));
//            }
//        });
    }


    /**
     * Updates the summary of a preference.
     */
    private void updatePrefSummary(SharedPreferences sharedPreferences, String key) {
        String lastConnect = sharedPreferences.getString(key, "");
        Log.d(TAG, "updatePrefSummary with value: " + lastConnect);
        if (Objects.equals(lastConnect, "")) {
            findPreference(key).setSummary(getResources().getString(R.string.connection_test_summary_neverconnected));
        } else {
            findPreference(key).setSummary(getResources().getString(R.string.connection_test_summary_lastconnected) + ": " + lastConnect);
        }
    }

    /**
     * This is automatically called if a SharedPreference changes.
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, key + " changed.");
        //Only handle summary changes for preferences without SimpleSummaryProvider.
        if (key.equals("pref_test_connection")) {
            updatePrefSummary(sharedPreferences, key);
        }
    }

//    /**
//     * Example for a callback in an inner class. This is used to 'remove'/hide some callbacks.
//     * This particular example is not that useful, though.
//     */
//    abstract class CustomGetLightsCallback implements GetLightsCallback {
//
//        @Override
//        public abstract void onSuccess(Response<List<Light>> response);
//
//        @Override
//        public abstract void onForbidden(Error error);
//
//        @Override
//        public void onEverytime() {
//            //Demo: Nothing should happen here.
//        }
//
//        @Override
//        public abstract void onNetworkFailure(Call<List<Light>> call, Throwable throwable);
//
//        @Override
//        public abstract void onInvalidResponseObject(Call<List<Light>> call, Throwable throwable);
//
//        @Override
//        public abstract void onInvalidErrorObject();
//    }

}
