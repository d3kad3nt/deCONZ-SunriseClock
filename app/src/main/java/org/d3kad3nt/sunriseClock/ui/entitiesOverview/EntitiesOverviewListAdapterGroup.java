package org.d3kad3nt.sunriseClock.ui.entitiesOverview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import org.d3kad3nt.sunriseClock.backend.data.model.group.UIGroup;
import org.d3kad3nt.sunriseClock.databinding.EntitiesOverviewListElementGroupBinding;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class EntitiesOverviewListAdapterGroup
        extends ListAdapter<UIGroup, EntitiesOverviewListAdapterGroup.GroupViewHolder> {

    private final ClickListeners clickListeners;

    public EntitiesOverviewListAdapterGroup(final ClickListeners clickListeners) {
        super(new DiffCallback());
        this.clickListeners = clickListeners;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupViewHolder(EntitiesOverviewListElementGroupBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        UIGroup group = getItem(position);
        LogUtil.v("Triggering full (re)bind of group data for groupId %d.", group.getId());
        holder.bind(group);
        holder.itemView.setTag(group);
    }

    public interface ClickListeners {

        /**
         * Navigates to the group detail screen, providing detailed information for this group.
         *
         * @param view View representing the group card.
         * @param groupId The unique identifier for this group.
         * @param groupName Name of the group.
         */
        void onGroupCardClick(View view, long groupId, String groupName);
    }

    static class DiffCallback extends DiffUtil.ItemCallback<UIGroup> {

        /** Used to determine structural changes between old and new list (additions/removals/position changes). */
        @Override
        public boolean areItemsTheSame(@NonNull UIGroup oldItem, @NonNull UIGroup newItem) {
            return oldItem.getId() == newItem.getId();
        }

        /**
         * Determines if the particular item was updated. Only called when {@link DiffCallback#areItemsTheSame} returned
         * true.
         */
        @Override
        public boolean areContentsTheSame(@NonNull UIGroup oldItem, @NonNull UIGroup newItem) {
            boolean result = oldItem.equals(newItem);
            if (!result) {
                LogUtil.d(
                        "Recyclerview determined that group with groupId %d was changed and its GroupViewHolder content must be updated.",
                        oldItem.getId());
            }
            return result;
        }
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {

        private final EntitiesOverviewListElementGroupBinding binding;

        GroupViewHolder(@NonNull EntitiesOverviewListElementGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Avoid creating and setting the onClickListeners everytime the GroupViewHolder is
            // bound.
            binding.setCardClickListener(new CardClickListener());
        }

        void bind(UIGroup item) {
            bindName(item.getName());

            binding.executePendingBindings();
        }

        void bindName(String name) {
            binding.setGroupName(name);
        }

        public class CardClickListener implements View.OnClickListener {

            @Override
            public void onClick(final View view) {
                // Because this is used as part of ConcatAdapter, we cannot use the typical
                // getAbsoluteAdapterPosition() to get the position of a ViewHolder in the adapter.
                UIGroup group = getItem(getBindingAdapterPosition());
                clickListeners.onGroupCardClick(view, group.getId(), group.getName());
            }
        }
    }
}
