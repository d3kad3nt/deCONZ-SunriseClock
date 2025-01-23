package org.d3kad3nt.sunriseClock.ui.light;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.d3kad3nt.sunriseClock.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.databinding.LightListElementBinding;

public class LightsListAdapter extends ListAdapter<UILight, LightsListAdapter.ViewHolder> {

    private static final String TAG = "LightsListAdapter";

    private final ClickListeners clickListeners;

    public LightsListAdapter(final ClickListeners clickListeners) {
        super(new LightDiffCallback());
        this.clickListeners = clickListeners;
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
        holder.bind(light);
        holder.itemView.setTag(light);
    }

    public interface ClickListeners {
        void onCardClick(View view, long lightId, String lightName);
        void onSwitchCheckedChanged(long lightId, boolean state);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LightListElementBinding binding;

        ViewHolder(@NonNull LightListElementBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(UILight item) {
            binding.setCardClickListener(new CardClickListener());
            binding.setSwitchCheckedChangedListener(new SwitchCheckedChangedListener());
            binding.setLight(item);
            binding.executePendingBindings();
        }

        public class CardClickListener implements View.OnClickListener {
            @Override
            public void onClick(final View view) {
                UILight light = getItem(getAbsoluteAdapterPosition());
                clickListeners.onCardClick(view, light.getLightId(), light.getName());
            }
        }

        public class SwitchCheckedChangedListener implements CompoundButton.OnCheckedChangeListener {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {
                UILight light = getItem(getAbsoluteAdapterPosition());
                clickListeners.onSwitchCheckedChanged(light.getLightId(), isChecked);
            }
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