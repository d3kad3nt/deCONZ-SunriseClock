package org.d3kad3nt.sunriseClock.ui.endpoint.endpointInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.d3kad3nt.sunriseClock.databinding.EndpointInfoFragmentBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EndpointInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EndpointInfoFragment extends Fragment {

    private EndpointInfoViewModel viewModel;

    public static EndpointInfoFragment newInstance() {
        return new EndpointInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        EndpointInfoFragmentBinding binding = EndpointInfoFragmentBinding.inflate(inflater, container, false);
        EndpointInfoFragmentArgs args = EndpointInfoFragmentArgs.fromBundle(requireArguments());
        long endpointID = args.getEndpointID();
        viewModel = new ViewModelProvider(requireActivity()).get(EndpointInfoViewModel.class);
        viewModel.getEndpoint(endpointID).observe(getViewLifecycleOwner(), endpointConfig -> {
            if (endpointConfig != null) {
                binding.setEndpoint(endpointConfig);
            }
        });
        return binding.getRoot();
    }
}
