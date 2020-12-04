package org.d3kad3nt.sunriseClock.ui.mainWindow;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import org.d3kad3nt.sunriseClock.model.endpoint.BaseEndpoint;

import java.util.List;


public class EndpointSelectorAdapter extends ArrayAdapter<BaseEndpoint> {

    public EndpointSelectorAdapter(@NonNull Context context, int resource, @NonNull List<BaseEndpoint> objects) {
        super(context, resource, objects);
    }

    public EndpointSelectorAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public void submitList(List<BaseEndpoint> configList){
        clear();
        for (BaseEndpoint config: configList){
            add(config);
        }
    }
}
