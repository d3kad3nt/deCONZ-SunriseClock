package org.d3kad3nt.sunriseClock.ui.mainWindow;

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

import org.d3kad3nt.sunriseClock.databinding.EndpointsFragmentBinding;
import org.d3kad3nt.sunriseClock.model.endpoint.EndpointConfig;
import org.d3kad3nt.sunriseClock.ui.viewModel.EndpointsViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EndpointsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EndpointsFragment extends Fragment {

    private static final String TAG = "EndpointsFragment";
    private EndpointsViewModel viewModel;

    private  EndpointsListAdapter adapter;

    public static EndpointsFragment newInstance() {
        return new EndpointsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        EndpointsFragmentBinding binding = EndpointsFragmentBinding.inflate(inflater, container, false);
        adapter = new EndpointsListAdapter();
        binding.recyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(requireActivity()).get(EndpointsViewModel.class);
        viewModel.getEndpoints().observe(getViewLifecycleOwner(), new Observer<List<EndpointConfig>>() {
            @Override
            public void onChanged(List<EndpointConfig> endpointConfigList) {
                if (!endpointConfigList.isEmpty()) {
                    adapter.submitList(endpointConfigList);
                } else {
                    Log.d(TAG,"No Endpoints found");
                }
            }
        });
        return binding.getRoot();
    }

}
