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

import org.d3kad3nt.sunriseClock.databinding.LightListElementBinding;
import org.d3kad3nt.sunriseClock.data.model.light.BaseLight;
import org.d3kad3nt.sunriseClock.data.model.light.LightID;


public class LightsListAdapter extends ListAdapter<BaseLight, LightsListAdapter.ViewHolder> {

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
        BaseLight light = getItem(position);
        holder.bind(createOnClickListener(light.getUUID()), light);
        holder.itemView.setTag(light);
    }

    private View.OnClickListener createOnClickListener(LightID lightID) {
        return v -> Navigation.findNavController(v).navigate(
                LightsFragmentDirections.actionLightsToLightInfo(lightID));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private LightListElementBinding binding;

        ViewHolder(@NonNull LightListElementBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(View.OnClickListener listener, BaseLight item) {
            binding.setClickListener(listener);
            binding.setLight(item);
            binding.executePendingBindings();
        }
    }

    static class LightDiffCallback extends DiffUtil.ItemCallback<BaseLight> {

        @Override
        public boolean areItemsTheSame(@NonNull BaseLight oldItem, @NonNull BaseLight newItem) {
            //TODO: use real UUID
            return oldItem.getUUID().equals(newItem.getUUID());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull BaseLight oldItem, @NonNull BaseLight newItem) {
            return oldItem == newItem;
        }
    }
}
