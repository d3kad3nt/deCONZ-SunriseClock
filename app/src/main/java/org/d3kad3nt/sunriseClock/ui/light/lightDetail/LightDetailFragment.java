package org.d3kad3nt.sunriseClock.ui.light.lightDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.d3kad3nt.sunriseClock.databinding.LightDetailFragmentBinding;
import org.d3kad3nt.sunriseClock.data.remote.common.Status;
import org.d3kad3nt.sunriseClock.data.model.light.LightID;

public class LightDetailFragment extends Fragment {

    private LightDetailViewModel viewModel;

    public static LightDetailFragment newInstance() {
        return new LightDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LightDetailFragmentBinding binding = LightDetailFragmentBinding.inflate(inflater, container, false);
        LightDetailFragmentArgs args = LightDetailFragmentArgs.fromBundle(requireArguments());
        LightID lightID = args.getLight();
        viewModel = new ViewModelProvider(requireActivity()).get(LightDetailViewModel.class);
        viewModel.getLight(lightID).observe(getViewLifecycleOwner(), baseLightResource -> {
            if (baseLightResource.getStatus().equals(Status.SUCCESS)) {
                binding.setLight(baseLightResource.getData());
            }
        });
        binding.switchOnState.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (binding.getLight() != null){
                viewModel.setLightOnState(binding.getLight(), isChecked);
            }
        }));
        return binding.getRoot();
    }
}
