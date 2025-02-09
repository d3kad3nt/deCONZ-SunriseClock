package org.d3kad3nt.sunriseClock.ui.light;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.data.model.resource.Status;
import org.d3kad3nt.sunriseClock.databinding.LightsFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.MainActivity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LightsFragment extends Fragment implements LightsListAdapter.ClickListeners {

    private static final String TAG = "LightsFragment";
    private final LightsState lightsState = new LightsState();
    private LightsFragmentBinding binding;
    private LightsViewModel viewModel;
    private Spinner endpointSpinner;
    private LightsListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(LightsViewModel.class);
        adapter = new LightsListAdapter(this);

        binding = LightsFragmentBinding.inflate(inflater, container, false);
        binding.setLightsState(lightsState);
        binding.recyclerView.setAdapter(adapter);

        viewModel.getLights().observe(getViewLifecycleOwner(), new Observer<Resource<List<UILight>>>() {
            @Override
            public void onChanged(Resource<List<UILight>> listResource) {
                Log.d(TAG, listResource.getStatus().toString());
                if (listResource.getStatus().equals(Status.SUCCESS) && listResource.getData() != null) {
                    lightsState.clearError();
                    List<UILight> list = listResource.getData();
                    Collections.sort(list, new Comparator<>() {
                        @Override
                        public int compare(final UILight uiLight, final UILight uiLight2) {
                            return uiLight.getName().compareTo(uiLight2.getName());
                        }
                    });
                    adapter.submitList(list);
                } else if (listResource.getStatus().equals(Status.ERROR)) {
                    lightsState.setError(getResources().getString(R.string.noLights_title),
                        listResource.getMessage());
                }
            }
        });

        addEndpointSelector();

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
        removeFromToolbar(endpointSpinner);
        binding = null;
    }

    private void addEndpointSelector() {
        endpointSpinner = new Spinner(getContext());
        EndpointSelectorAdapter adapter =
            new EndpointSelectorAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item);
        endpointSpinner.setAdapter(adapter);
        endpointSpinner.setOnItemSelectedListener(new EndpointSelectedListener(getContext()));

        viewModel.getEndpoints().observe(getViewLifecycleOwner(), configList -> {
            adapter.submitCollection(configList);
            if (!configList.isEmpty()) {
                addToToolbar(endpointSpinner);
                lightsState.clearError();
            } else {
                removeFromToolbar(endpointSpinner);
                lightsState.setError(getResources().getString(R.string.noEndpoint_title),
                    getResources().getString(R.string.noEndpoint_message));
            }
        });

        viewModel.getSelectedEndpoint().observe(getViewLifecycleOwner(), endpointConfig -> {

            Observer<? super List<IEndpointUI>> endpointSelector = new Observer<List<IEndpointUI>>() {
                @Override
                public void onChanged(List<IEndpointUI> endpointConfigs) {
                    endpointSpinner.setSelection(endpointConfigs.indexOf(endpointConfig));
                }
            };
            viewModel.getEndpoints().observe(getViewLifecycleOwner(), endpointSelector);
            viewModel.getEndpoints().removeObserver(endpointSelector);
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
    public void onCardClick(View view, final long lightId, final String lightName) {
        Navigation.findNavController(view).navigate(LightsFragmentDirections.actionLightsToLightDetail(lightId, lightName));
    }

    @Override
    public void onSwitchCheckedChange(final long lightId, final boolean state) {
        viewModel.setLightOnState(lightId, state);
    }

    @Override
    public void onSliderTouch(final long lightId, @IntRange(from = 0, to = 100) final int brightness, final boolean state) {
        viewModel.setLightBrightness(lightId, brightness);
    }
}
