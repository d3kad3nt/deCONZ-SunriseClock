package org.d3kad3nt.sunriseClock.ui.light;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.Slider;

import org.d3kad3nt.sunriseClock.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.databinding.LightListElementBinding;
import org.d3kad3nt.sunriseClock.util.LogUtil;

import java.util.List;

public class LightsListAdapter extends ListAdapter<UILight, LightsListAdapter.ViewHolder> {

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

    // Full bind.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LogUtil.v("Triggering full (re)bind of light data for lightId %d.", getItem(position).getLightId());

        UILight light = getItem(position);
        holder.bind(light);
        holder.itemView.setTag(light);
    }

    // If the payloads list is not empty, the ViewHolder is currently bound to old data and Adapter may run an
    // efficient partial update using the payload info. If the payload is empty, Adapter must run a full bind.
    // The payloads list is constructed by overwriting getChangePayload() from DiffUtil.ItemCallback.
    // By using a partial bind, we can prevent the light card from "flickering". This is the default animation for
    // updates.
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position,
                                 @NonNull final List<Object> payloads) {

        if (!payloads.isEmpty()) {
            LogUtil.d("Triggering partial instead of full rebind of light data for lightId %d.",
                getItem(position).getLightId());
            for (Object payload : payloads) {
                if (!(payload instanceof final UILight.UILightChangePayload lightPayload)) {
                    LogUtil.w("Unexpected payload type: %s", payload.getClass().getName());
                    continue;
                }
                LogUtil.v("Triggering partial rebind of %s state for lightId %d.",
                    lightPayload.getType().name(), getItem(position).getLightId());
                lightPayload.bindVariable((id, value) -> holder.binding.setVariable(id, value));
            }
            holder.binding.executePendingBindings();
        } else {
            // When payload list is empty or we don't have logic to handle a given type, default to full bind.
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    public interface ClickListeners {

        /**
         * Navigates to the light detail screen, providing detailed information for this light.
         *
         * @param view      View representing the light card.
         * @param lightId   The unique identifier for this light.
         * @param lightName Name of the light.
         */
        void onCardClick(View view, long lightId, String lightName);

        /**
         * Turns the light on or off.
         *
         * @param lightId The unique identifier for this light.
         * @param state   Whether the light should be turned on (true) or off (false).
         */
        void onSwitchCheckedChange(long lightId, boolean state);

        /**
         * Changes the brightness of the light, identified by the given lightId. If the light is not already on, it
         * should be turned on before changing the brightness level.
         *
         * @param lightId    The unique identifier for this light.
         * @param brightness Desired light brightness, ranging from 0 (lowest) to 100 (highest).
         * @param state      Whether the light is on (true) or off (false).
         */
        void onSliderTouch(long lightId, @IntRange(from = 0, to = 100) int brightness, boolean state);
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
         * {@link LightsListAdapter.LightDiffCallback#areItemsTheSame} returned true.
         */
        @Override
        public boolean areContentsTheSame(@NonNull UILight oldItem, @NonNull UILight newItem) {
            boolean result = oldItem.equals(newItem);
            if (!result) {
                LogUtil.d("Recyclerview determined that light with lightId %d was " +
                    "changed and its ViewHolder content must be updated.", oldItem.getLightId());
            }
            return result;
        }

        /**
         * Return the particular field that changed in the item. Only called when
         * {@link LightsListAdapter.LightDiffCallback#areContentsTheSame} returned false.
         * <p>
         * This populates the payloads list for onBindViewHolder.
         */
        @Nullable
        @Override
        public Object getChangePayload(@NonNull final UILight oldItem, @NonNull final UILight newItem) {
            return UILight.getSingleChangePayload(oldItem, newItem);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LightListElementBinding binding;

        ViewHolder(@NonNull LightListElementBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Avoid creating and setting the onClickListeners everytime the ViewHolder is bound.
            binding.setCardClickListener(new CardClickListener());
            binding.setSwitchCheckedChangeListener(new SwitchCheckedChangeListener());
            binding.setSliderTouchListener(new SliderTouchListener());
        }

        void bind(@NonNull UILight item) {
            binding.setLightName(item.getName());
            binding.setLightIsReachable(item.getIsReachable());
            binding.setLightIsSwitchable(item.getIsSwitchable());
            binding.setLightIsOn(item.getIsOn());
            binding.setLightIsDimmable(item.getIsDimmable());
            binding.setLightBrightness(item.getBrightness());

            binding.executePendingBindings();
        }

        public class CardClickListener implements View.OnClickListener {

            @Override
            public void onClick(final View view) {
                UILight light = getItem(getAbsoluteAdapterPosition());
                clickListeners.onCardClick(view, light.getLightId(), light.getName());
            }
        }

        public class SwitchCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {
                UILight light = getItem(getAbsoluteAdapterPosition());
                clickListeners.onSwitchCheckedChange(light.getLightId(), isChecked);
            }
        }

        // An OnChangeListener would report every single change, even when still dragging.
        // OnSliderTouchListener reports only once, after the slider touch is released.
        public class SliderTouchListener implements Slider.OnSliderTouchListener {

            @Override
            public void onStartTrackingTouch(@NonNull final Slider slider) {
            }

            @Override
            public void onStopTrackingTouch(@NonNull final Slider slider) {
                UILight light = getItem(getAbsoluteAdapterPosition());
                clickListeners.onSliderTouch(light.getLightId(), (int) slider.getValue(), light.getIsOn());
            }
        }
    }
}