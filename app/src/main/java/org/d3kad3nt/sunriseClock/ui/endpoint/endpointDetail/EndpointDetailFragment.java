package org.d3kad3nt.sunriseClock.ui.endpoint.endpointDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.MutableCreationExtras;

import org.d3kad3nt.sunriseClock.data.repository.EndpointRepository;
import org.d3kad3nt.sunriseClock.data.repository.SettingsRepository;
import org.d3kad3nt.sunriseClock.databinding.EndpointDetailFragmentBinding;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class EndpointDetailFragment extends Fragment {

    private EndpointDetailFragmentBinding binding;
    private EndpointDetailViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LogUtil.d("Show endpoint detail view");
        long endpointID = EndpointDetailFragmentArgs.fromBundle(requireArguments()).getEndpointID(); // id from navigation

        // Initialize viewModel with endpoint id and inject the endpoint and settings repository.
        // By injecting the repository, the viewModel no longer needs the Application or Context.
        MutableCreationExtras viewModelDependencies = new MutableCreationExtras();
        viewModelDependencies.set(EndpointDetailViewModel.ENDPOINT_REPOSITORY_KEY, EndpointRepository.getInstance(requireContext()));
        viewModelDependencies.set(EndpointDetailViewModel.SETTINGS_REPOSITORY_KEY, SettingsRepository.getInstance(requireContext()));
        viewModelDependencies.set(EndpointDetailViewModel.ENDPOINT_ID_KEY, endpointID);

        // Use custom factory to initialize the viewModel (instead of using new ViewModelProvider(this).get(EndpointDetailViewModel.class)).
        // For viewModel older than 2.5.0 ViewModelProvider.Factory had to be extended.
        viewModel = new ViewModelProvider(this.getViewModelStore(),
            ViewModelProvider.Factory.from(EndpointDetailViewModel.initializer), viewModelDependencies).get(EndpointDetailViewModel.class);

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
