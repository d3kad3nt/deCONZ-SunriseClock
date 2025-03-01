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
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class EndpointDetailFragment extends Fragment {

    private EndpointDetailFragmentBinding binding;
    private EndpointDetailViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LogUtil.d("Show endpoint detail view");
        long endpointID =
            EndpointDetailFragmentArgs.fromBundle(requireArguments()).getEndpointID(); // id from navigation
        // Use custom factory to initialize viewModel with endpoint id (instead of using new ViewModelProvider
        // (this).get(EndpointDetailViewModel.class))
        viewModel = new ViewModelProvider(this,
            new EndpointDetailViewModelFactory(requireActivity().getApplication(), endpointID)).get(
            EndpointDetailViewModel.class);
        binding = EndpointDetailFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.setViewModel(viewModel);
        // Specify the fragment view as the lifecycle owner of the binding. This is used so that the binding can
        // observe LiveData updates.
        binding.setLifecycleOwner(getViewLifecycleOwner());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
