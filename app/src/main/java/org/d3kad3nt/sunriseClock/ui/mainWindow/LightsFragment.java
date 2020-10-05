package org.d3kad3nt.sunriseClock.ui.mainWindow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.d3kad3nt.sunriseClock.databinding.LightsFragmentBinding;
import org.d3kad3nt.sunriseClock.model.endpoint.remoteApi.Resource;
import org.d3kad3nt.sunriseClock.model.endpoint.remoteApi.Status;
import org.d3kad3nt.sunriseClock.model.light.BaseLight;
import org.d3kad3nt.sunriseClock.ui.MainActivity;
import org.d3kad3nt.sunriseClock.ui.viewModel.LightsViewModel;

import java.util.List;
import java.util.Objects;

public class LightsFragment extends Fragment {

    private LightsViewModel viewModel;

    public static LightsFragment newInstance() {
        return new LightsFragment();
    }

    public LightsListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LightsFragmentBinding binding = LightsFragmentBinding.inflate(inflater, container, false);
        adapter = new LightsListAdapter();
        binding.recyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(requireActivity()).get(LightsViewModel.class);
        viewModel.getLight().observe(getViewLifecycleOwner(), new Observer<Resource<List<BaseLight>>>() {
            @Override
            public void onChanged(Resource<List<BaseLight>> baseLightResource) {
                Log.d("LightInfoFragment", baseLightResource.getStatus().toString());
                if (baseLightResource.getStatus().equals(Status.SUCCESS) && baseLightResource.getData() != null) {
                    Log.d("LightInfoFragment", baseLightResource.getData().get(0).friendlyName + ": Name");
                    adapter.submitList(baseLightResource.getData());
                } else if (baseLightResource.getStatus().equals(Status.ERROR)) {
                    Log.d("LightInfoFragment", Objects.requireNonNull(baseLightResource.getMessage()));
                }
            }
        });
        addEnpointSelector();
        return binding.getRoot();
    }

    private void addEnpointSelector() {
        Spinner endpointSpinner = new Spinner(getContext());
        String[] endpoints = new String[]{"Deconz","MQTT","Apple"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,endpoints);
        endpointSpinner.setAdapter(adapter);
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).getToolbar().addView(endpointSpinner);
        }
    }
}
