package org.d3kad3nt.sunriseClock.ui.light;

import android.annotation.SuppressLint;
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

    public LightsListAdapter() {
        super(new LightDiffCallback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LightListElementBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UILight light = getItem(position);
        holder.bind(createOnClickListener(light.getLightId()), light);
        holder.itemView.setTag(light);
    }

    private View.OnClickListener createOnClickListener(long lightID) {
        return v -> Navigation.findNavController(v).navigate(
                LightsFragmentDirections.actionLightsToLightDetail(lightID));
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

        @Override
        public boolean areItemsTheSame(@NonNull UILight oldItem, @NonNull UILight newItem) {
            //TODO: use real UUID
            return oldItem.getLightId() == newItem.getLightId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull UILight oldItem, @NonNull UILight newItem) {
            return oldItem == newItem;
        }
    }
}
