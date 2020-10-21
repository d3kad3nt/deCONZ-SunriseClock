package org.d3kad3nt.sunriseClock.ui.mainWindow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.databinding.LightsFragmentBinding;
import org.d3kad3nt.sunriseClock.model.endpoint.remoteApi.Resource;
import org.d3kad3nt.sunriseClock.model.endpoint.remoteApi.Status;
import org.d3kad3nt.sunriseClock.model.light.BaseLight;
import org.d3kad3nt.sunriseClock.ui.MainActivity;
import org.d3kad3nt.sunriseClock.ui.viewModel.LightsViewModel;

import java.util.List;

public class LightsFragment extends Fragment {

    private static final String TAG = "LightsFragment";
    private LightsViewModel viewModel;
    private Spinner endpointSpinner;
    private final LightsState lightsState = new LightsState();

    private  LightsListAdapter adapter;

    public static LightsFragment newInstance() {
        return new LightsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LightsFragmentBinding binding = LightsFragmentBinding.inflate(inflater, container, false);
        binding.setLightsState(lightsState);
        adapter = new LightsListAdapter();
        binding.recyclerView.setAdapter(adapter);
        viewModel = new ViewModelProvider(requireActivity()).get(LightsViewModel.class);
        viewModel.getLights().observe(getViewLifecycleOwner(), new Observer<Resource<List<BaseLight>>>() {
            @Override
            public void onChanged(Resource<List<BaseLight>> baseLightResource) {
                Log.d(TAG, baseLightResource.getStatus().toString());
                if (baseLightResource.getStatus().equals(Status.SUCCESS) && baseLightResource.getData() != null) {
                    lightsState.clearError();
                    adapter.submitList(baseLightResource.getData());
                } else if (baseLightResource.getStatus().equals(Status.ERROR)) {
                    lightsState.setError(getResources().getString(R.string.noLights_title),baseLightResource.getMessage());
                }
            }
        });
        addEndpointSelector();
        return binding.getRoot();
    }

    private void addEndpointSelector() {
        endpointSpinner  = new Spinner(getContext());
        EndpointSelectorAdapter adapter = new EndpointSelectorAdapter(getContext(),
                android.R.layout.simple_spinner_dropdown_item);
        endpointSpinner.setAdapter(adapter);
        endpointSpinner.setOnItemSelectedListener(new EndpointSelectedListener(getContext()));

        viewModel.getEndpoints().observe(getViewLifecycleOwner(), configList -> {
            adapter.submitList(configList);
            if (!configList.isEmpty()){
                addToToolbar(endpointSpinner);
                lightsState.clearError();
            }else{
                removeFromToolbar(endpointSpinner);
                lightsState.setError(getResources().getString(R.string.noEndpoint_title),getResources().getString(R.string.noEndpoint_message));
            }
        });

        viewModel.getSelectedEndpoint().observe(getViewLifecycleOwner(), endpointConfig -> {
            viewModel.getEndpoints().observe(getViewLifecycleOwner(),endpointConfigs -> {
                endpointSpinner.setSelection(
                    endpointConfigs.indexOf(endpointConfig));
            });
        });
    }

    private void removeFromToolbar(View view) {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).getToolbar().removeView(view);
        }
    }

    private void addToToolbar(View view) {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
            if (toolbar.indexOfChild(view) == -1) {
                toolbar.addView(view);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeFromToolbar(endpointSpinner);
    }
}
