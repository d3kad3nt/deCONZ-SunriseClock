package org.d3kad3nt.sunriseClock.ui.endpoint.endpointDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.d3kad3nt.sunriseClock.databinding.EndpointDetailFragmentBinding;

public class EndpointDetailFragment extends Fragment {

    private EndpointDetailViewModel viewModel;

    public static EndpointDetailFragment newInstance() {
        return new EndpointDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        EndpointDetailFragmentBinding binding = EndpointDetailFragmentBinding.inflate(inflater, container, false);
        EndpointDetailFragmentArgs args = EndpointDetailFragmentArgs.fromBundle(requireArguments());
        long endpointID = args.getEndpointID();
        viewModel = new ViewModelProvider(requireActivity()).get(EndpointDetailViewModel.class);
        viewModel.getEndpoint(endpointID).observe(getViewLifecycleOwner(), endpointConfig -> {
            if (endpointConfig != null) {
                binding.setEndpoint(endpointConfig);
            }
        });
        return binding.getRoot();
    }
}
