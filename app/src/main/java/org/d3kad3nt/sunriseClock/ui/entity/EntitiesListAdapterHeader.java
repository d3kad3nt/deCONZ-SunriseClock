package org.d3kad3nt.sunriseClock.ui.entity;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.d3kad3nt.sunriseClock.databinding.EntitiesListElementHeaderBinding;

public class EntitiesListAdapterHeader extends RecyclerView.Adapter<EntitiesListAdapterHeader.HeaderViewHolder> {

    private final String headerText;

    public EntitiesListAdapterHeader(final String headerText) {
        this.headerText = headerText;
    }

    @NonNull
    @Override
    public HeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HeaderViewHolder(
                EntitiesListElementHeaderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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

        private final EntitiesListElementHeaderBinding binding;

        HeaderViewHolder(@NonNull EntitiesListElementHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(String headerText) {
            binding.setHeaderText(headerText);
            binding.executePendingBindings();
        }
    }
}
