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
import org.d3kad3nt.sunriseClock.ui.viewModel.LightInfoViewModel;

public class LightInfoFragment extends Fragment {

    private LightInfoViewModel mViewModel;

    public static LightInfoFragment newInstance() {
        return new LightInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("LightInfoFragment", "onCreateView");
        return inflater.inflate(R.layout.light_info_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LightInfoViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("LightInfoFragment", "onViewCreated");
        super.onViewCreated(view,savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(LightInfoViewModel.class);
    }
}
