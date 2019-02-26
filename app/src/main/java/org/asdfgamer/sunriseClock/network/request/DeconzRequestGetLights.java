package org.asdfgamer.sunriseClock.network.request;

import android.net.Uri;

import org.asdfgamer.sunriseClock.network.DeconzApiEndpoints;
import org.asdfgamer.sunriseClock.network.listener.ErrorListener;
import org.asdfgamer.sunriseClock.network.listener.GUIListener;
import org.asdfgamer.sunriseClock.network.listener.GetConfListener;
import org.asdfgamer.sunriseClock.network.listener.GetLightsListener;
import org.asdfgamer.sunriseClock.network.response.DeconzResponseGetConf;
import org.asdfgamer.sunriseClock.network.response.DeconzResponseGetLights;
import org.asdfgamer.sunriseClock.network.response.IResponseListenerJSONObject;

public class DeconzRequestGetLights extends DeconzRequest implements IResponseListenerJSONObject {

    public DeconzRequestGetLights(Uri baseUrl, String apiKey) {
        super(baseUrl, apiKey);
    }

    private static final String TAG = "DeconzRequestGetConf";

    @Override
    public void init() {
        super.setBaseCommandPath(super.getBaseCommandPath().buildUpon().appendPath(DeconzApiEndpoints.LIGHTS.getApiEndpoint()).build());
    }

    @Override
    public void fire(GUIListener listener) {
        DeconzResponseGetLights deconzResponseGetLights = new DeconzResponseGetLights();
        GetLightsListener getLightsListener = new GetLightsListener(listener, deconzResponseGetLights);

        getFromDeconz(getLightsListener, new ErrorListener(listener, deconzResponseGetLights));
    }

    @Override
    public void fire() {
        getFromDeconz();
    }
}
