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

import org.d3kad3nt.sunriseClock.data.model.ListItem;
import org.d3kad3nt.sunriseClock.data.model.ListItemType;
import org.d3kad3nt.sunriseClock.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.databinding.LightListElementBinding;
import org.d3kad3nt.sunriseClock.util.LogUtil;

import java.util.List;

public class LightsListAdapter extends ListAdapter<ListItem, RecyclerView.ViewHolder> {

    private final ClickListeners clickListeners;

    public LightsListAdapter(final ClickListeners clickListeners) {
        super(new LightDiffCallback());
        this.clickListeners = clickListeners;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (ListItemType.valueOf(viewType)) {
            case ListItemType.LIGHT:
                return new ViewHolder(
                    LightListElementBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            default:
                throw new UnsupportedOperationException(String.format("Unknown Type: %d", viewType));
        }
    }

    // Full bind (not partial).
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getType(position)) {
            case ListItemType.LIGHT -> {
                UILight light = (UILight) getItem(position);
                LogUtil.v("Triggering full (re)bind of light data for lightId %d.", light.getLightId());
                ViewHolder lightholder = (ViewHolder) holder;
                lightholder.bind(light);
                lightholder.itemView.setTag(light);
            }
        }
    }

    // If the payloads list is not empty, the ViewHolder is currently bound to old data and Adapter may run an
    // efficient partial update using the payload info. If the payload is empty, Adapter must run a full bind.
    // The payloads list is constructed by overwriting getChangePayload() from DiffUtil.ItemCallback.
    // By using a partial bind, we can prevent the light card from "flickering". This is the default animation for
    // updates.
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position,
                                 @NonNull final List<Object> payloads) {
        boolean successfull_bind = switch (getType(position)) {
            case LIGHT -> {
                yield onPartialBindLightViewHolder((ViewHolder) holder, (UILight) getItem(position), payloads);
            }
            default -> {
                throw new UnsupportedOperationException();
            }
        };
        if (!successfull_bind) {
            onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(final int position) {
        return getItem(position).getType().ordinal();
    }

    @NonNull
    private ListItemType getType(int position) {
        int viewType = getItemViewType(position);
        return ListItemType.valueOf(viewType);
    }

    private boolean onPartialBindLightViewHolder(@NonNull final ViewHolder holder, final UILight light,
                                                 @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            LogUtil.d("Triggering partial instead of full rebind of light data for lightId %d.",
                light.getLightId());
            if (payloads.get(0) instanceof final UILight.UILightChangePayload.LightOn lightState) {
                LogUtil.v("Triggering partial rebind of light isOn state for lightId %d.",
                    light.getLightId());
                holder.bindIsOn(lightState.isOn);
            } else if (payloads.get(0) instanceof final UILight.UILightChangePayload.LightBrightness lightState) {
                LogUtil.v("Triggering partial rebind of light brightness for lightId %d.",
                    light.getLightId());
                holder.bindBrightness(lightState.brightness);
            } else {
                LogUtil.w("Requested partial rebind of light data but updating this field is not yet implemented.");
            }
            holder.binding.executePendingBindings();
            return true;
        } else {
            // When payload list is empty or we don't have logic to handle a given type, default to full bind.
            return false;
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

    static class LightDiffCallback extends DiffUtil.ItemCallback<ListItem> {

        /**
         * Used to determine structural changes between old and new list (additions/removals/position changes).
         */
        @Override
        public boolean areItemsTheSame(@NonNull ListItem oldItem, @NonNull ListItem newItem) {
            if (oldItem instanceof UILight oldLight && newItem instanceof UILight newLight) {
                return oldLight.getLightId() == newLight.getLightId();
            }
            return false;
        }

        /**
         * Determines if the particular item was updated. Only called when
         * {@link LightDiffCallback#areItemsTheSame} returned true.
         */
        @Override
        public boolean areContentsTheSame(@NonNull ListItem oldItem, @NonNull ListItem newItem) {
            boolean result = oldItem.equals(newItem);
//            if (!result) {
//                LogUtil.d("Recyclerview determined that light with lightId %d was " +
//                    "changed and its ViewHolder content must be updated.", oldItem.getLightId());
//            }
            return result;
        }

        /**
         * Return the particular field that changed in the item. Only called when
         * {@link LightDiffCallback#areContentsTheSame} returned false.
         * <p>
         * This populates the payloads list for onBindViewHolder.
         */
        @Nullable
        @Override
        public Object getChangePayload(@NonNull final ListItem oldItem, @NonNull final ListItem newItem) {
            if (oldItem instanceof UILight oldLight && newItem instanceof UILight newLight) {
                return UILight.getSingleChangePayload(oldLight, newLight);
            }
            return null;
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

        void bind(UILight item) {
            bindName(item.getName());
            bindIsReachable(item.getIsReachable());
            bindIsSwitchable(item.getIsSwitchable());
            bindIsOn(item.getIsOn());
            bindIsDimmable(item.getIsDimmable());
            bindBrightness(item.getBrightness());

            binding.executePendingBindings();
        }

        void bindName(String name) {
            binding.setLightName(name);
        }

        void bindIsReachable(boolean isOn) {
            binding.setLightIsReachable(isOn);
        }

        void bindIsSwitchable(boolean isSwitchable) {
            binding.setLightIsSwitchable(isSwitchable);
        }

        void bindIsOn(boolean isOn) {
            binding.setLightIsOn(isOn);
        }

        void bindIsDimmable(boolean isDimmable) {
            binding.setLightIsDimmable(isDimmable);
        }

        void bindBrightness(int brightness) {
            binding.setLightBrightness(brightness);
        }

        public class CardClickListener implements View.OnClickListener {

            @Override
            public void onClick(final View view) {
                switch (getType(getAbsoluteAdapterPosition())) {
                    case LIGHT -> {
                        UILight light = (UILight) getItem(getAbsoluteAdapterPosition());
                        clickListeners.onCardClick(view, light.getLightId(), light.getName());
                    }
                    default ->
                        throw new IllegalStateException("Unexpected value: " + getType(getAbsoluteAdapterPosition()));
                }
            }
        }

        public class SwitchCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {
                switch (getType(getAbsoluteAdapterPosition())) {
                    case LIGHT -> {
                        UILight light = (UILight) getItem(getAbsoluteAdapterPosition());
                        clickListeners.onSwitchCheckedChange(light.getLightId(), isChecked);
                    }
                    default ->
                        throw new IllegalStateException("Unexpected value: " + getType(getAbsoluteAdapterPosition()));
                }
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
                switch (getType(getAbsoluteAdapterPosition())) {
                    case LIGHT -> {
                        UILight light = (UILight) getItem(getAbsoluteAdapterPosition());
                        clickListeners.onSliderTouch(light.getLightId(), (int) slider.getValue(), light.getIsOn());
                    }
                    default ->
                        throw new IllegalStateException("Unexpected value: " + getType(getAbsoluteAdapterPosition()));
                }
            }
        }
    }
}