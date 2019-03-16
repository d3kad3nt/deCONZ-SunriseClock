package org.asdfgamer.sunriseClock.maintabs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.asdfgamer.sunriseClock.R;
import org.asdfgamer.sunriseClock.network.lights.model.Light;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LightsAdapter extends RecyclerView.Adapter<LightsAdapter.MyViewHolder> {
    private List<Light> lights;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(final View view) {
            view.setActivated(!view.isActivated());

            notifyDataSetChanged();
        }
    };

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView lightName;
        TextView lightType;
        TextView lightManufacturer;

        MyViewHolder(View cardView) {
            super(cardView);
            lightName = cardView.findViewById(R.id.light_name);
            lightType = cardView.findViewById(R.id.light_type);
            lightManufacturer = cardView.findViewById(R.id.light_manufacturer);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    LightsAdapter(List<Light> lightList) {
        lights = lightList;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public LightsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_light, parent, false);
        cardView.setOnClickListener(mOnClickListener);
        return new MyViewHolder(cardView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Light light = lights.get(position);
        holder.lightName.setText(light.getName());
        holder.lightType.setText(light.getType());
        holder.lightManufacturer.setText(light.getManufacturername());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return lights.size();
    }

    /**
     * Deletes all old Lights and adds the new Lights to the RecyclerView.
     *
     * @param newLights A List with the new Lights that should be displayed in the RecyclerView.
     */
    void updateData(List<Light> newLights) {
        lights.clear();
        lights.addAll(newLights);
        notifyDataSetChanged();
    }

}
