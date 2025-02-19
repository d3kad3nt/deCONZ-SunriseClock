package org.d3kad3nt.sunriseClock.ui.endpoint;

import android.os.Bundle;
import android.util.Log;
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

import java.util.List;

/**
 * A simple {@link Fragment} subclass. Use the {@link EndpointsFragment#newInstance} factory method to create an
 * instance of this fragment.
 */
public class EndpointsFragment extends Fragment implements EndpointsListAdapter.ClickListeners {

    private static final String TAG = "EndpointsFragment";
    private EndpointsViewModel viewModel;

    private EndpointsListAdapter adapter;

    public static EndpointsFragment newInstance() {
        return new EndpointsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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
                    Log.d(TAG, "No Endpoints found");
                }
            }
        });
        addAddEndpointListener(binding);
        return binding.getRoot();
    }

    private void addAddEndpointListener(EndpointsFragmentBinding binding) {
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v)
                    .navigate(EndpointsFragmentDirections.actionEndpointsToEndpointAddFragment());
            }
        });
    }

    @Override
    public void onCardClick(final View view, final IEndpointUI endpoint) {
        Navigation.findNavController(view).navigate(EndpointsFragmentDirections.actionEndpointsToEndpointDetail(endpoint.getId(),
            endpoint.getName()));
    }
}
