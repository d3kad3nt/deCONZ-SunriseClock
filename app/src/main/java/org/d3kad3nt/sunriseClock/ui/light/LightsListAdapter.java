package org.d3kad3nt.sunriseClock.ui.light;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.d3kad3nt.sunriseClock.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.databinding.LightListElementBinding;

public class LightsListAdapter extends ListAdapter<UILight, LightsListAdapter.ViewHolder> {

    private static final String TAG = "LightsListAdapter";

    public LightsListAdapter() {
        super(new LightDiffCallback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LightListElementBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UILight light = getItem(position);
        holder.bind(createOnClickListener(light.getLightId()), light);
        holder.itemView.setTag(light);
    }

    private View.OnClickListener createOnClickListener(long lightID) {
        return v -> Navigation.findNavController(v)
                .navigate(LightsFragmentDirections.actionLightsToLightDetail(lightID));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final LightListElementBinding binding;

        ViewHolder(@NonNull LightListElementBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(View.OnClickListener listener, UILight item) {
            binding.setClickListener(listener);
            binding.setLight(item);
            binding.executePendingBindings();
        }
    }

    static class LightDiffCallback extends DiffUtil.ItemCallback<UILight> {

        /**
         * Used to determine structural changes between old and new list (additions/removals/position changes).
         */
        @Override
        public boolean areItemsTheSame(@NonNull UILight oldItem, @NonNull UILight newItem) {
            return oldItem.getLightId() == newItem.getLightId();
        }

        /**
         * Determines if the particular item was updated. Only called when
         * {@link LightsListAdapter.LightDiffCallback#areItemsTheSame}
         * returned true.
         */
        @Override
        public boolean areContentsTheSame(@NonNull UILight oldItem, @NonNull UILight newItem) {
            boolean result = oldItem.equals(newItem);
            if (!result) {
                Log.d(TAG, "Recyclerview determined that light with lightId " + oldItem.getLightId() + " was " +
                           "changed and its ViewHolder content must be updated.");
            }
            return result;
        }
        // Optional getChangePayload() could be overwritten. This is called when areItemsTheSame() returns true for
        // two items and areContentsTheSame() returns false for them to get a payload about the change.
    }
}