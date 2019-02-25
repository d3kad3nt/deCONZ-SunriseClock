package org.asdfgamer.sunriseClock.maintabs;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.asdfgamer.sunriseClock.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LightsAdapter extends RecyclerView.Adapter<LightsAdapter.MyViewHolder> {
    private String[] mDataset;

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
    LightsAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public LightsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_light, parent, false);

        return new MyViewHolder(cardView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.lightName.setText(mDataset[position]);
        //TODO: Temp text until actual light data is retrieved from deconz.
        holder.lightType.setText("Type: Temp");
        holder.lightManufacturer.setText("Manufacturer: Temp");
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
