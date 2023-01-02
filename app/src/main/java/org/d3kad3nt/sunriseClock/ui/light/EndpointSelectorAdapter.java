package org.d3kad3nt.sunriseClock.ui.light;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import org.d3kad3nt.sunriseClock.data.model.endpoint.IEndpointUI;

import java.util.Collection;
import java.util.List;

public class EndpointSelectorAdapter extends ArrayAdapter<IEndpointUI> {

    public EndpointSelectorAdapter(@NonNull Context context, int resource, @NonNull List<IEndpointUI> objects) {
        super(context, resource, objects);
    }

    public EndpointSelectorAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public void submitCollection(Collection<IEndpointUI> configList) {
        clear();
        for (IEndpointUI config : configList) {
            add(config);
        }
    }
}
