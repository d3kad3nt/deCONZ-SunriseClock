package org.d3kad3nt.sunriseClock.ui.light;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.d3kad3nt.sunriseClock.databinding.LightInfoFragmentBinding;
import org.d3kad3nt.sunriseClock.data.remote.common.Status;
import org.d3kad3nt.sunriseClock.data.model.light.LightID;

public class LightInfoFragment extends Fragment {

    private LightInfoViewModel viewModel;

    public static LightInfoFragment newInstance() {
        return new LightInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LightInfoFragmentBinding binding = LightInfoFragmentBinding.inflate(inflater, container, false);
        LightInfoFragmentArgs args = LightInfoFragmentArgs.fromBundle(requireArguments());
        LightID lightID = args.getLight();
        viewModel = new ViewModelProvider(requireActivity()).get(LightInfoViewModel.class);
        viewModel.getLight(lightID).observe(getViewLifecycleOwner(), baseLightResource -> {
            if (baseLightResource.getStatus().equals(Status.SUCCESS)) {
                binding.setLight(baseLightResource.getData());
            }
        });
        return binding.getRoot();
    }
}
