package org.d3kad3nt.sunriseClock.ui.entitiesOverview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import org.d3kad3nt.sunriseClock.backend.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.databinding.EntitiesOverviewListElementLightBinding;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class EntitiesOverviewListAdapterLight
        extends ListAdapter<UILight, EntitiesOverviewListAdapterLight.LightViewHolder> {

    private final ClickListeners clickListeners;

    public EntitiesOverviewListAdapterLight(final ClickListeners clickListeners) {
        super(new DiffCallback());
        this.clickListeners = clickListeners;
    }

    @NonNull
    @Override
    public LightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LightViewHolder(EntitiesOverviewListElementLightBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LightViewHolder holder, int position) {
        UILight light = getItem(position);
        LogUtil.v("Triggering full (re)bind of light data for lightId %d.", light.getId());
        holder.bind(light);
        holder.itemView.setTag(light);
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
    }

    static class DiffCallback extends DiffUtil.ItemCallback<UILight> {

        /** Used to determine structural changes between old and new list (additions/removals/position changes). */
        @Override
        public boolean areItemsTheSame(@NonNull UILight oldItem, @NonNull UILight newItem) {
            return oldItem.getId() == newItem.getId();
        }

        /**
         * Determines if the particular item was updated. Only called when {@link DiffCallback#areItemsTheSame} returned
         * true.
         */
        @Override
        public boolean areContentsTheSame(@NonNull UILight oldItem, @NonNull UILight newItem) {
            boolean result = oldItem.equals(newItem);
            if (!result) {
                LogUtil.d(
                        "Recyclerview determined that light with lightId %d was changed and its LightViewHolder content must be updated.",
                        oldItem.getId());
            }
            return result;
        }
    }

    public class LightViewHolder extends RecyclerView.ViewHolder {

        private final EntitiesOverviewListElementLightBinding binding;

        LightViewHolder(@NonNull EntitiesOverviewListElementLightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Avoid creating and setting the onClickListeners everytime the LightViewHolder is
            // bound.
            binding.setCardClickListener(new CardClickListener());
        }

        void bind(UILight item) {
            bindName(item.getName());

            binding.executePendingBindings();
        }

        void bindName(String name) {
            binding.setLightName(name);
        }

        public class CardClickListener implements View.OnClickListener {

            @Override
            public void onClick(final View view) {
                // Because this is used as part of ConcatAdapter, we cannot use the typical
                // getAbsoluteAdapterPosition() to get the position of a ViewHolder in the adapter.
                UILight light = getItem(getBindingAdapterPosition());
                clickListeners.onLightCardClick(view, light.getId(), light.getName());
            }
        }
    }
}
