package org.d3kad3nt.sunriseClock.ui.mainWindow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.d3kad3nt.sunriseClock.databinding.EndpointAddFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.viewModel.EndpointAddViewModel;

public class EndpointAddFragment extends Fragment {

    private EndpointAddViewModel viewModel;

    public static EndpointInfoFragment newInstance() {
        return new EndpointInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        EndpointAddFragmentBinding binding = EndpointAddFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(EndpointAddViewModel.class);
        return binding.getRoot();
    }

}
