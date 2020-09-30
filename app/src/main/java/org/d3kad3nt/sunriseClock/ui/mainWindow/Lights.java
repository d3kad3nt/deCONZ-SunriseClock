package org.d3kad3nt.sunriseClock.ui.mainWindow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.ui.viewModel.LightsViewModel;

public class Lights extends Fragment {

    private LightsViewModel mViewModel;

    public static Lights newInstance() {
        return new Lights();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lights_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(LightsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("LightInfoFragment", "onViewCreated");
        super.onViewCreated(view,savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(LightsViewModel.class);
        mViewModel.getLight().observe(getViewLifecycleOwner(),baseLightResource -> {
            Log.d("LightInfoFragment", baseLightResource.getData().get(0).friendlyName);
        });
    }

}
