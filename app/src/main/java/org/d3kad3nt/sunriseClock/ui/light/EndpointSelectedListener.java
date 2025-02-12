package org.d3kad3nt.sunriseClock.ui.light;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import org.d3kad3nt.sunriseClock.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.data.repository.SettingsRepository;

public class EndpointSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {

    private final Context context;

    public EndpointSelectedListener(Context context) {
        this.context = context;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        IEndpointUI selected = (IEndpointUI) parent.getItemAtPosition(position);
        SettingsRepository settingsRepository = SettingsRepository.getInstance(context);
        settingsRepository.setActiveEndpoint(selected.getId());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
