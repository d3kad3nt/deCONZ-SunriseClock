package org.d3kad3nt.sunriseClock.ui.light;

import android.content.Context;
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
import org.d3kad3nt.sunriseClock.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.databinding.LightsFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.MainActivity;
import org.jetbrains.annotations.Contract;

import java.util.List;

public class LightsFragment extends Fragment {

    private static final String TAG = "LightsFragment";
    private final LightsState lightsState = new LightsState();
    private LightsViewModel viewModel;
    private Spinner endpointSpinner;
    private LightsListAdapter adapter;

    @NonNull
    @Contract(" -> new")
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
        viewModel.getLights().observe(getViewLifecycleOwner(), new Observer<Resource<List<UILight>>>() {
            @Override
            public void onChanged(Resource<List<UILight>> listResource) {
                Log.d(TAG, listResource.getStatus().toString());
                if (listResource.getData() != null) {
                    adapter.submitList(listResource.getData());
                }
                switch (listResource.getStatus()) {
                    case SUCCESS:
                        lightsState.clearError();
                        break;
                    case ERROR:
                        String errorMsg = getResources().getString(R.string.noLights_title);
                        lightsState.setError(errorMsg, listResource.getMessage());
                        break;
                }
            }
        });
        addEndpointSelector();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeFromToolbar(endpointSpinner);
    }

    private void addEndpointSelector() {
        Context context = getContext();
        if (context == null) {
            throw new IllegalStateException("No Context found");
        }
        endpointSpinner = new Spinner(context);
        EndpointSelectorAdapter adapter =
            new EndpointSelectorAdapter(context, android.R.layout.simple_spinner_dropdown_item);
        endpointSpinner.setAdapter(adapter);
        endpointSpinner.setOnItemSelectedListener(new EndpointSelectedListener(context));

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
                public void onChanged(@NonNull List<IEndpointUI> endpointConfigs) {
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
}
