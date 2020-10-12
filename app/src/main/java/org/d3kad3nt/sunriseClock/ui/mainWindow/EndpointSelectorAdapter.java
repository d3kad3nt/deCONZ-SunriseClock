package org.d3kad3nt.sunriseClock.ui.mainWindow;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import org.d3kad3nt.sunriseClock.model.endpoint.EndpointConfig;

import java.util.List;


public class EndpointSelectorAdapter extends ArrayAdapter<EndpointConfig> {

    public EndpointSelectorAdapter(@NonNull Context context, int resource, @NonNull List<EndpointConfig> objects) {
        super(context, resource, objects);
    }

    public EndpointSelectorAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public void submitList(List<EndpointConfig> configList){
        clear();
        for (EndpointConfig config: configList){
            add(config);
        }
    }
}
