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
import org.d3kad3nt.sunriseClock.backend.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.databinding.EndpointListElementBinding;

public class EndpointsListAdapter extends ListAdapter<IEndpointUI, EndpointsListAdapter.EndpointListViewHolder> {

    private final EndpointsViewModel viewModel;
    private final ClickListeners clickListeners;

    public EndpointsListAdapter(final EndpointsViewModel viewModel, final ClickListeners clickListeners) {
        super(new EndpointDiffCallback());
        this.viewModel = viewModel;
        this.clickListeners = clickListeners;
    }

    @NonNull
    @Override
    public EndpointListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EndpointListViewHolder(
                EndpointListElementBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EndpointListViewHolder holder, int position) {
        IEndpointUI endpoint = getItem(position);
        holder.bind(endpoint);
        holder.itemView.setTag(endpoint);
    }

    public interface ClickListeners {

        /**
         * Navigates to the endpoint detail screen, providing detailed information for this endpoint.
         *
         * @param view View representing the endpoint card.
         * @param endpointId Id of the endpoint.
         * @param endpointName Name of the endpoint.
         */
        void onCardClick(View view, long endpointId, String endpointName);
    }

    static class EndpointDiffCallback extends DiffUtil.ItemCallback<IEndpointUI> {

        @Override
        public boolean areItemsTheSame(@NonNull IEndpointUI oldItem, @NonNull IEndpointUI newItem) {
            // TODO: use real UUID
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull IEndpointUI oldItem, @NonNull IEndpointUI newItem) {
            return oldItem.equals(newItem);
        }
    }

    public class EndpointListViewHolder extends RecyclerView.ViewHolder {

        private final EndpointListElementBinding binding;
        private IEndpointUI endpoint;

        EndpointListViewHolder(@NonNull EndpointListElementBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(IEndpointUI item) {
            binding.setCardClickListener(new CardClickListener());
            binding.setEndpoint(item);
            binding.setRadioCheckedChangeListener(new RadioCheckedChangeListener());
            this.endpoint = item;
            binding.setEndpointSelected(viewModel.isSelectedEndpoint(item.getId()));
            binding.executePendingBindings();
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
                // We only react to user interactions, not programmatic changes.
                // isPressed() is true only when the user is actively pressing the button.
                // This check prevents an infinite loop when the checked state is changed programmatically
                // during a rebind.
                if (checkedState && compoundButton.isPressed()) {
                    viewModel.setSelectedEndpoint(endpoint.getId());
                }
            }
        }
    }
}
