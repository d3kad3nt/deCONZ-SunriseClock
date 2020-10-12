package org.d3kad3nt.sunriseClock.ui.mainWindow;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import org.d3kad3nt.sunriseClock.model.endpoint.EndpointConfig;

import java.util.List;


public class EndpointSelectorAdapter extends ArrayAdapter<String> {

    public EndpointSelectorAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    public void submitList(List<EndpointConfig> configList){
        clear();
        for (EndpointConfig config: configList){
            //TODO change to name
            add(config.id + "");
        }
    }
}
