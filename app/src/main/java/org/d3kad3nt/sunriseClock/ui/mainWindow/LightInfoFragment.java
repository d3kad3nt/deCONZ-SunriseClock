package org.d3kad3nt.sunriseClock.ui.mainWindow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import org.d3kad3nt.sunriseClock.databinding.LightInfoFragmentBinding;
import org.d3kad3nt.sunriseClock.model.endpoint.remoteApi.Status;
import org.d3kad3nt.sunriseClock.model.light.LightID;
import org.d3kad3nt.sunriseClock.ui.viewModel.LightInfoViewModel;

public class LightInfoFragment extends Fragment {

    private LightInfoViewModel viewModel;

    public static LightInfoFragment newInstance() {
        return new LightInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        LightInfoFragmentBinding binding = LightInfoFragmentBinding.inflate(inflater, container, false);
        Object lightID_obj = getArguments().get("Light");
        if (!(lightID_obj instanceof LightID)){
            NavHostFragment.findNavController(this).navigateUp();
            return binding.getRoot();
        }
        LightID lightID = (LightID)lightID_obj;
        LightInfoFragmentArgs args = LightInfoFragmentArgs.fromBundle(requireArguments());
        viewModel = new ViewModelProvider(requireActivity()).get(LightInfoViewModel.class);
        viewModel.getLight(lightID).observe(getViewLifecycleOwner(), baseLightResource -> {
            if (baseLightResource.getStatus().equals(Status.SUCCESS)) {
                binding.setLight(baseLightResource.getData());
            }
        });
        return binding.getRoot();
    }
}
