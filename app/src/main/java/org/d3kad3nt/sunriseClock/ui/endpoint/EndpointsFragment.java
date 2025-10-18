package org.d3kad3nt.sunriseClock.ui.endpoint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import java.util.List;
import org.d3kad3nt.sunriseClock.backend.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.databinding.EndpointsFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.util.BaseFragment;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class EndpointsFragment extends BaseFragment<EndpointsFragmentBinding, EndpointsViewModel>
        implements EndpointsListAdapter.ClickListeners {

    private EndpointsListAdapter adapter;

    @Override
    protected EndpointsFragmentBinding getViewBinding(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        return EndpointsFragmentBinding.inflate(inflater, container, false);
    }

    @Override
    protected Class<EndpointsViewModel> getViewModelClass() {
        return EndpointsViewModel.class;
    }

    @Override
    protected void bindVars(final EndpointsFragmentBinding binding) {
        addAddEndpointListener(binding);

        adapter = new EndpointsListAdapter(viewModel, this);
        binding.recyclerView.setAdapter(adapter);
        viewModel.getEndpoints().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(List<IEndpointUI> endpointConfigList) {
                if (!endpointConfigList.isEmpty()) {
                    adapter.submitList(endpointConfigList);
                } else {
                    LogUtil.d("No Endpoints found");
                }
            }
        });

        // When the selected endpoint changes, we need to re-bind all visible items in the recycler
        // view to update their radio buttons.
        viewModel.getSelectedEndpointId().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(final Long aLong) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected LifecycleOwner getLifecycleOwner() {
        return getViewLifecycleOwner();
    }

    @Override
    protected ViewModelProvider getViewModelProvider() {
        return new ViewModelProvider(this);
    }

    private void addAddEndpointListener(@NonNull EndpointsFragmentBinding binding) {
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("Navigate to endpoint creation view");
                Navigation.findNavController(v)
                        .navigate(EndpointsFragmentDirections.actionEndpointsToEndpointAddFragment());
            }
        });
    }

    @Override
    public void onCardClick(final View view, final long endpointId, String endpointName) {
        LogUtil.d("Navigate to endpoint detail view for endpoint %s (Id %d)", endpointName, endpointId);
        Navigation.findNavController(view)
                .navigate(EndpointsFragmentDirections.actionEndpointsToEndpointDetail(endpointId, endpointName));
    }
}
