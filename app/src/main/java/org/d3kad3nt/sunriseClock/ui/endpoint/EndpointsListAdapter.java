package org.d3kad3nt.sunriseClock.ui.endpoint;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.d3kad3nt.sunriseClock.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.databinding.EndpointListElementBinding;

public class EndpointsListAdapter extends ListAdapter<IEndpointUI, EndpointsListAdapter.ViewHolder> {

    private final EndpointsViewModel viewModel;

    private CompoundButton selectedRadioButton = null;

    private final ClickListeners clickListeners;

    public EndpointsListAdapter(final EndpointsViewModel viewModel, final ClickListeners clickListeners) {
        super(new EndpointDiffCallback());
        this.viewModel = viewModel;
        this.clickListeners = clickListeners;
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
        holder.bind(endpoint);
        holder.itemView.setTag(endpoint);
    }

    public interface ClickListeners {

        /**
         * Navigates to the endpoint detail screen, providing detailed information for this endpoint.
         *
         * @param view         View representing the endpoint card.
         * @param endpointId   Id of the endpoint.
         * @param endpointName Name of the endpoint.
         */
        void onCardClick(View view, long endpointId, String endpointName);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final EndpointListElementBinding binding;
        private IEndpointUI endpoint;

        ViewHolder(@NonNull EndpointListElementBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(IEndpointUI item) {
            binding.setCardClickListener(new CardClickListener());
            binding.setEndpoint(item);
            binding.setRadioCheckedChangeListener(new RadioCheckedChangeListener());
            binding.executePendingBindings();
            this.endpoint = item;
            binding.setEndpointSelected(viewModel.isSelectedEndpoint(item.getId()));
        }

        public class CardClickListener implements View.OnClickListener {

            @Override
            public void onClick(final View view) {
                IEndpointUI endpoint = getItem(getAbsoluteAdapterPosition());
                clickListeners.onCardClick(view, endpoint.getId(), endpoint.getName());
            }
        }

        public class RadioCheckedChangeListener implements RadioButton.OnCheckedChangeListener {

            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean checkedState) {
                if (!checkedState) {
                    return;
                }
                if (selectedRadioButton != null) {
                    selectedRadioButton.setChecked(false);
                }
                selectedRadioButton = compoundButton;
                viewModel.setSelectedEndpoint(endpoint.getId());
            }
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
