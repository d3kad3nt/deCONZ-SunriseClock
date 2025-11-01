package org.d3kad3nt.sunriseClock.ui.home;

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
import java.util.List;
import org.d3kad3nt.sunriseClock.backend.data.model.ListItem;
import org.d3kad3nt.sunriseClock.backend.data.model.ListItemType;
import org.d3kad3nt.sunriseClock.backend.data.model.group.UIGroup;
import org.d3kad3nt.sunriseClock.backend.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.databinding.HomeListElementGroupBinding;
import org.d3kad3nt.sunriseClock.databinding.HomeListElementLightBinding;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class HomeListAdapter extends ListAdapter<ListItem, RecyclerView.ViewHolder> {

    private final ClickListeners clickListeners;

    public HomeListAdapter(final ClickListeners clickListeners) {
        super(new DiffCallback());
        this.clickListeners = clickListeners;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return switch (ListItemType.valueOf(viewType)) {
            case ListItemType.LIGHT ->
                new LightViewHolder(
                        HomeListElementLightBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            case ListItemType.GROUP ->
                new GroupViewHolder(
                        HomeListElementGroupBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            default -> throw new IllegalStateException("Unexpected value: " + ListItemType.valueOf(viewType));
        };
    }

    // Full bind (not partial).
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getType(position)) {
            case ListItemType.LIGHT -> {
                UILight light = (UILight) getItem(position);
                LogUtil.v("Triggering full (re)bind of light data for lightId %d.", light.getId());
                LightViewHolder lightViewHolder = (LightViewHolder) holder;
                lightViewHolder.bind(light);
                lightViewHolder.itemView.setTag(light);
            }
            case ListItemType.GROUP -> {
                UIGroup group = (UIGroup) getItem(position);
                LogUtil.v("Triggering full (re)bind of group data for groupId %d.", group.getId());
                GroupViewHolder groupViewHolder = (GroupViewHolder) holder;
                groupViewHolder.bind(group);
                groupViewHolder.itemView.setTag(group);
            }
        }
    }

    // If the payloads list is not empty, the ViewHolder is currently bound to old data and Adapter may run an efficient
    // partial update using the payload info. If the payload is empty, Adapter must run a full bind.
    // The payloads list is constructed by overwriting getChangePayload() from DiffUtil.ItemCallback.
    // By using a partial bind, we can prevent the card from "flickering". This is the default animation for updates.
    @Override
    public void onBindViewHolder(
            @NonNull final RecyclerView.ViewHolder holder, final int position, @NonNull final List<Object> payloads) {
        boolean successfulPartialBind =
                switch (getType(position)) {
                    case ListItemType.LIGHT ->
                        onPartialBindLightViewHolder((LightViewHolder) holder, (UILight) getItem(position), payloads);
                    case ListItemType.GROUP ->
                        onPartialBindGroupViewHolder((GroupViewHolder) holder, (UIGroup) getItem(position), payloads);
                    default -> throw new IllegalStateException("Unexpected value: " + getType(position));
                };
        if (!successfulPartialBind) {
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

    private boolean onPartialBindLightViewHolder(
            @NonNull final LightViewHolder holder, final UILight light, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            LogUtil.d("Triggering partial instead of full rebind of light data for lightId %d.", light.getId());
            if (payloads.get(0) instanceof final UILight.UILightChangePayload.LightOn lightState) {
                LogUtil.v("Triggering partial rebind of light getIsOn state for lightId %d.", light.getId());
                holder.bindIsOn(lightState.isOn);
            } else if (payloads.get(0) instanceof final UILight.UILightChangePayload.LightBrightness lightState) {
                LogUtil.v("Triggering partial rebind of light brightness for lightId %d.", light.getId());
                holder.bindBrightness(lightState.brightness);
            } else {
                LogUtil.w("Requested partial rebind of light data but updating this field is not yet implemented.");
            }
            holder.binding.executePendingBindings();
            return true;
        } else {
            // When payload list is empty or we don't have logic to handle a given type, default to
            // full bind.
            return false;
        }
    }

    private boolean onPartialBindGroupViewHolder(
            @NonNull final GroupViewHolder holder, final UIGroup group, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            LogUtil.d("Triggering partial instead of full rebind of group data for groupId %d.", group.getId());
            if (payloads.get(0) instanceof final UIGroup.UIGroupChangePayload.GroupOnAny groupState) {
                LogUtil.v("Triggering partial rebind of group getIsOnAny state for groupId %d.", group.getId());
                holder.bindIsOnAny(groupState.isOnAny);
            } else {
                LogUtil.w("Requested partial rebind of group data but updating this field is not yet implemented.");
            }
            holder.binding.executePendingBindings();
            return true;
        } else {
            // When payload list is empty or we don't have logic to handle a given type, default to
            // full bind.
            return false;
        }
    }

    public interface ClickListeners {

        /**
         * Navigates to the light detail screen, providing detailed information for this light.
         *
         * @param view View representing the light card.
         * @param lightId The unique identifier for this light.
         * @param lightName Name of the light.
         */
        void onLightCardClick(View view, long lightId, String lightName);

        /**
         * Turns the light on or off.
         *
         * @param lightId The unique identifier for this light.
         * @param state Whether the light should be turned on (true) or off (false).
         */
        void onLightSwitchCheckedChange(long lightId, boolean state);

        /**
         * Changes the brightness of the light, identified by the given lightId. If the light is not already on, it
         * should be turned on before changing the brightness level.
         *
         * @param lightId The unique identifier for this light.
         * @param brightness Desired light brightness, ranging from 0 (lowest) to 100 (highest).
         * @param state Whether the light is on (true) or off (false).
         */
        void onLightSliderTouch(long lightId, @IntRange(from = 0, to = 100) int brightness, boolean state);

        /**
         * Navigates to the group detail screen, providing detailed information for this group.
         *
         * @param view View representing the group card.
         * @param groupId The unique identifier for this group.
         * @param groupName Name of the group.
         */
        void onGroupCardClick(View view, long groupId, String groupName);

        /**
         * Turns all lights in the group on or off.
         *
         * @param groupId The unique identifier for this group.
         * @param state Whether the group should be turned on (true) or off (false).
         */
        void onGroupSwitchCheckedChange(long groupId, boolean state);
    }

    static class DiffCallback extends DiffUtil.ItemCallback<ListItem> {

        /** Used to determine structural changes between old and new list (additions/removals/position changes). */
        @Override
        public boolean areItemsTheSame(@NonNull ListItem oldItem, @NonNull ListItem newItem) {
            if (oldItem instanceof UILight oldLight && newItem instanceof UILight newLight) {
                return oldLight.getId() == newLight.getId();
            } else if (oldItem instanceof UIGroup oldGroup && newItem instanceof UIGroup newGroup) {
                return oldGroup.getId() == newGroup.getId();
            } else {
                throw new IllegalStateException(String.format(
                        "areItemsTheSame() does not support %s and %s.", oldItem.getType(), newItem.getType()));
            }
        }

        /**
         * Determines if the particular item was updated. Only called when {@link DiffCallback#areItemsTheSame} returned
         * true.
         */
        @Override
        public boolean areContentsTheSame(@NonNull ListItem oldItem, @NonNull ListItem newItem) {
            boolean result = oldItem.equals(newItem);
            if (oldItem instanceof UILight oldLight && newItem instanceof UILight && !result) {
                LogUtil.d(
                        "Recyclerview determined that light with lightId %d was changed and its LightViewHolder "
                                + "content must be updated.",
                        oldLight.getId());
            } else if (oldItem instanceof UIGroup oldGroup && newItem instanceof UIGroup && !result) {
                LogUtil.d(
                        "Recyclerview determined that group with groupId %d was changed and its GroupViewHolder "
                                + "content must be updated.",
                        oldGroup.getId());
            }
            return result;
        }

        /**
         * Return the particular field that changed in the item. Only called when
         * {@link DiffCallback#areContentsTheSame} returned false.
         *
         * <p>This populates the payloads list for onBindViewHolder.
         */
        @Nullable
        @Override
        public Object getChangePayload(@NonNull final ListItem oldItem, @NonNull final ListItem newItem) {
            if (oldItem instanceof UILight oldLight && newItem instanceof UILight newLight) {
                return UILight.getSingleChangePayload(oldLight, newLight);
            } else if (oldItem instanceof UIGroup oldGroup && newItem instanceof UIGroup newGroup) {
                return UIGroup.getSingleChangePayload(oldGroup, newGroup);
            } else {
                throw new IllegalStateException(String.format(
                        "getChangePayload() does not support %s and %s.", oldItem.getType(), newItem.getType()));
            }
        }
    }

    public class LightViewHolder extends RecyclerView.ViewHolder {

        private final HomeListElementLightBinding binding;

        LightViewHolder(@NonNull HomeListElementLightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Avoid creating and setting the onClickListeners everytime the LightViewHolder is
            // bound.
            binding.setCardClickListener(new CardClickListener());
            binding.setSwitchCheckedChangeListener(new SwitchCheckedChangeListener());
            binding.setSliderTouchListener(new SliderTouchListener());
        }

        void bind(@NonNull UILight item) {
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
                UILight light = (UILight) getItem(getAbsoluteAdapterPosition());
                clickListeners.onLightCardClick(view, light.getId(), light.getName());
            }
        }

        public class SwitchCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {
                UILight light = (UILight) getItem(getAbsoluteAdapterPosition());
                if (light.getIsOn() == isChecked) {
                    // This happens, when a viewHolder is reused and a new Light is bound to it.
                    LogUtil.v("Suppressing onCheckedChanged event for light %d.", light.getId());
                    return;
                }
                clickListeners.onLightSwitchCheckedChange(light.getId(), isChecked);
            }
        }

        // An OnChangeListener would report every single change, even when still dragging.
        // OnSliderTouchListener reports only once, after the slider touch is released.
        public class SliderTouchListener implements Slider.OnSliderTouchListener {

            @Override
            public void onStartTrackingTouch(@NonNull final Slider slider) {}

            @Override
            public void onStopTrackingTouch(@NonNull final Slider slider) {
                UILight light = (UILight) getItem(getAbsoluteAdapterPosition());
                clickListeners.onLightSliderTouch(light.getId(), (int) slider.getValue(), light.getIsOn());
            }
        }
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {

        private final HomeListElementGroupBinding binding;

        GroupViewHolder(@NonNull HomeListElementGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Avoid creating and setting the onClickListeners everytime the GroupViewHolder is
            // bound.
            binding.setCardClickListener(new GroupViewHolder.CardClickListener());
            binding.setSwitchCheckedChangeListener(new GroupViewHolder.SwitchCheckedChangeListener());
        }

        void bind(UIGroup item) {
            bindName(item.getName());
            bindIsOnAny(item.getIsOnAny());

            binding.executePendingBindings();
        }

        void bindName(String name) {
            binding.setGroupName(name);
        }

        void bindIsOnAny(boolean isOnAny) {
            binding.setGroupIsOnAny(isOnAny);
        }

        public class CardClickListener implements View.OnClickListener {

            @Override
            public void onClick(final View view) {
                UIGroup group = (UIGroup) getItem(getAbsoluteAdapterPosition());
                clickListeners.onGroupCardClick(view, group.getId(), group.getName());
            }
        }

        public class SwitchCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {
                UIGroup group = (UIGroup) getItem(getAbsoluteAdapterPosition());
                if (group.getIsOnAny() == isChecked) {
                    // This happens, when a viewHolder is reused and a new Group is bound to it.
                    LogUtil.v("Suppressing onCheckedChanged event for group %d.", group.getId());
                    return;
                }
                clickListeners.onGroupSwitchCheckedChange(group.getId(), isChecked);
            }
        }
    }
}
