package org.d3kad3nt.sunriseClock.ui.endpoint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.d3kad3nt.sunriseClock.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.databinding.EndpointsFragmentBinding;
import org.d3kad3nt.sunriseClock.util.LogUtil;

import java.util.List;

public class EndpointsFragment extends Fragment implements EndpointsListAdapter.ClickListeners {

    private EndpointsViewModel viewModel;

    private EndpointsListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LogUtil.d("Show endpoint list view");
        EndpointsFragmentBinding binding = EndpointsFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(EndpointsViewModel.class);
        adapter = new EndpointsListAdapter(viewModel, this);
        binding.recyclerView.setAdapter(adapter);
        viewModel.getEndpoints().observe(getViewLifecycleOwner(), new Observer<List<IEndpointUI>>() {
            @Override
            public void onChanged(List<IEndpointUI> endpointConfigList) {
                if (!endpointConfigList.isEmpty()) {
                    adapter.submitList(endpointConfigList);
                } else {
                    LogUtil.d("No Endpoints found");
                }
            }
        });
        addAddEndpointListener(binding);
        return binding.getRoot();
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
