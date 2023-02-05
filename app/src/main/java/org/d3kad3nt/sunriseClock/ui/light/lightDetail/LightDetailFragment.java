package org.d3kad3nt.sunriseClock.ui.light.lightDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.d3kad3nt.sunriseClock.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.databinding.LightDetailFragmentBinding;

public class LightDetailFragment extends Fragment {

    private LightDetailFragmentBinding binding;
    private LightDetailViewModel viewModel;

    public static LightDetailFragment newInstance() {
        return new LightDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        long lightID = LightDetailFragmentArgs.fromBundle(requireArguments()).getLight(); // id from navigation
        // Use custom factory to initialize viewModel with light id (instead of using new ViewModelProvider(this)
        // .get(LightDetailViewModel.class))
        viewModel = new ViewModelProvider(this,
            new LightDetailViewModelFactory(requireActivity().getApplication(), lightID)).get(
            LightDetailViewModel.class);
        binding = LightDetailFragmentBinding.inflate(inflater, container, false);
        setLabel();
        return binding.getRoot();
    }

    private void setLabel() {
        viewModel.light.observe(getViewLifecycleOwner(), new Observer<Resource<UILight>>() {
            @Override
            public void onChanged(final Resource<UILight> uiLightResource) {
                AppCompatActivity activity = ((AppCompatActivity) getActivity());
                if (activity == null) {
                    return;
                }
                ActionBar actionBar = activity.getSupportActionBar();
                if (actionBar == null) {
                    return;
                }
                UILight light = uiLightResource.getData();
                if (light == null) {
                    return;
                }
                actionBar.setTitle(light.getName());
            }
        });
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
        binding = null;
    }
}
