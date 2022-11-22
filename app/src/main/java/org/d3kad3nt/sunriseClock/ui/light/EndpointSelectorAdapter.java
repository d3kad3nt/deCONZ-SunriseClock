package org.d3kad3nt.sunriseClock.ui.light;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import org.d3kad3nt.sunriseClock.data.model.endpoint.UIEndpoint;

import java.util.Collection;
import java.util.List;


public class EndpointSelectorAdapter extends ArrayAdapter<UIEndpoint> {

    public EndpointSelectorAdapter(@NonNull Context context, int resource, @NonNull List<UIEndpoint> objects) {
        super(context, resource, objects);
    }

    public EndpointSelectorAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public void submitCollection(Collection<UIEndpoint> configList){
        clear();
        for (UIEndpoint config: configList){
            add(config);
        }
    }
}
