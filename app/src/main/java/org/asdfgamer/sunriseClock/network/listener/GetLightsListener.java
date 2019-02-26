package org.asdfgamer.sunriseClock.network.listener;

import android.net.Uri;
import android.util.Log;

import org.asdfgamer.sunriseClock.network.response.DeconzResponseGetConf;
import org.asdfgamer.sunriseClock.network.response.DeconzResponseGetLights;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class GetLightsListener extends SuccessListener {
    private final DeconzResponseGetLights deconzResponse;


    public GetLightsListener(GUIListener guiListener, DeconzResponseGetLights response) {
        super(guiListener, response);
        this.deconzResponse = response;
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            deconzResponse.setTime(System.currentTimeMillis());

            Iterator<String> keys = response.keys();

            while (keys.hasNext()) {
                String lightId = keys.next();
                if (response.get(lightId) instanceof JSONObject) {
                    JSONObject jsonLight = (JSONObject) response.get(lightId);
                    DeconzResponseGetLights.DeconzLight light = deconzResponse.new DeconzLight();

                    light.setId(lightId);
                    light.setManufacturer(jsonLight.get("manufacturername").toString());
                    light.setModelid(jsonLight.get("modelid").toString());
                    light.setName(jsonLight.get("name").toString());
                    light.setType(jsonLight.get("type").toString());

                    deconzResponse.getLights().add(light);
                }
            }
            deconzResponse.setSuccess(true);
            getGuiListener().callback(deconzResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
