package org.d3kad3nt.sunriseClock.ui.mainWindow;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.d3kad3nt.sunriseClock.databinding.LightListElementBinding;
import org.d3kad3nt.sunriseClock.model.light.BaseLight;


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
        holder.bind(createOnClickListener(light.friendlyName), light);
        holder.itemView.setTag(light);
    }

    private View.OnClickListener createOnClickListener(String plantId) {
        return v -> Navigation.findNavController(v).navigate(
                LightsFragmentDirections.actionLightsToLightInfo());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private LightListElementBinding binding;

        ViewHolder(@NonNull LightListElementBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(View.OnClickListener listener, BaseLight item) {
            binding.setClickListener(listener);
            Log.d("Test", item.friendlyName);
            binding.setLight(item);
            binding.executePendingBindings();
        }
    }

    static class LightDiffCallback extends DiffUtil.ItemCallback<BaseLight> {

        @Override
        public boolean areItemsTheSame(@NonNull BaseLight oldItem, @NonNull BaseLight newItem) {
            //TODO: use real UUID
            return oldItem.friendlyName.equals(newItem.friendlyName);
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull BaseLight oldItem, @NonNull BaseLight newItem) {
            return oldItem == newItem;
        }
    }
}
