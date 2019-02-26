package org.asdfgamer.sunriseClock.network.listener;

import android.net.Uri;

import org.asdfgamer.sunriseClock.network.response.DeconzResponseGetConf;
import org.json.JSONException;
import org.json.JSONObject;

public class GetConfListener extends SuccessListener {
    private final DeconzResponseGetConf deconzResponse;


    public GetConfListener(GUIListener guiListener, DeconzResponseGetConf response) {
        super(guiListener, response);
        this.deconzResponse = response;
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            deconzResponse.setTime(System.currentTimeMillis());

            deconzResponse.setApiVersion(response.get("apiversion").toString());
            deconzResponse.setIp(response.get("ipaddress").toString());
            deconzResponse.setSuccess(true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        getGuiListener().callback(deconzResponse);
    }
}
