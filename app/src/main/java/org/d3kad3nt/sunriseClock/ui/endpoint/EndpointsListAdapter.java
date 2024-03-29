package org.d3kad3nt.sunriseClock.ui.endpoint;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.d3kad3nt.sunriseClock.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.databinding.EndpointListElementBinding;

public class EndpointsListAdapter extends ListAdapter<IEndpointUI, EndpointsListAdapter.ViewHolder> {

    public EndpointsListAdapter() {
        super(new EndpointDiffCallback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
            EndpointListElementBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IEndpointUI endpoint = getItem(position);
        holder.bind(createOnClickListener(endpoint.getId()), endpoint);
        holder.itemView.setTag(endpoint);
    }

    private View.OnClickListener createOnClickListener(long endpointID) {
        return v -> Navigation.findNavController(v)
            .navigate(EndpointsFragmentDirections.actionEndpointsToEndpointDetail(endpointID));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final EndpointListElementBinding binding;

        ViewHolder(@NonNull EndpointListElementBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(View.OnClickListener listener, IEndpointUI item) {
            binding.setClickListener(listener);
            binding.setEndpoint(item);
            binding.executePendingBindings();
        }
    }

    static class EndpointDiffCallback extends DiffUtil.ItemCallback<IEndpointUI> {

        @Override
        public boolean areItemsTheSame(@NonNull IEndpointUI oldItem, @NonNull IEndpointUI newItem) {
            //TODO: use real UUID
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull IEndpointUI oldItem, @NonNull IEndpointUI newItem) {
            return oldItem == newItem;
        }
    }
}
