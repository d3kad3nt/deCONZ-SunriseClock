package org.d3kad3nt.sunriseClock.ui.entitiesOverview;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.d3kad3nt.sunriseClock.databinding.EntitiesOverviewListElementHeaderBinding;

public class EntitiesOverviewListAdapterHeader
        extends RecyclerView.Adapter<EntitiesOverviewListAdapterHeader.HeaderViewHolder> {

    private final String headerText;

    public EntitiesOverviewListAdapterHeader(final String headerText) {
        this.headerText = headerText;
    }

    @NonNull
    @Override
    public HeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HeaderViewHolder(EntitiesOverviewListElementHeaderBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HeaderViewHolder holder, int position) {
        holder.bind(headerText);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final EntitiesOverviewListElementHeaderBinding binding;

        HeaderViewHolder(@NonNull EntitiesOverviewListElementHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(String headerText) {
            binding.setHeaderText(headerText);
            binding.executePendingBindings();
        }
    }
}
