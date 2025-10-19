package org.d3kad3nt.sunriseClock.ui.lightGroup.lightGroupDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.MutableCreationExtras;
import org.d3kad3nt.sunriseClock.backend.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.databinding.LightGroupDetailFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.util.BaseFragment;
import org.d3kad3nt.sunriseClock.ui.util.MenuHandler;

public class LightGroupDetailFragment extends BaseFragment<LightGroupDetailFragmentBinding, LightGroupDetailViewModel> {

    @Nullable
    @Override
    protected MenuHandler bindMenu() {
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        long groupId =
                LightGroupDetailFragmentArgs.fromBundle(requireArguments()).getGroupId();
        viewModel.loadGroup(groupId);

        binding.swipeRefreshLayout.setOnRefreshListener(() -> viewModel.refreshGroup());
    }

    @Override
    protected LightGroupDetailFragmentBinding getViewBinding(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LightGroupDetailFragmentBinding.inflate(inflater, container, false);
    }

    @Override
    protected Class<LightGroupDetailViewModel> getViewModelClass() {
        return LightGroupDetailViewModel.class;
    }

    @Override
    protected void bindVars(LightGroupDetailFragmentBinding binding) {
        binding.setViewModel(viewModel);
    }

    @Override
    protected LifecycleOwner getLifecycleOwner() {
        return getViewLifecycleOwner();
    }

    @Override
    protected ViewModelProvider getViewModelProvider() {
        MutableCreationExtras viewModelDependencies = new MutableCreationExtras();
        viewModelDependencies.set(
                LightGroupDetailViewModel.LIGHT_REPOSITORY_KEY, LightRepository.getInstance(requireContext()));

        return new ViewModelProvider(
                this.getViewModelStore(),
                ViewModelProvider.Factory.from(LightGroupDetailViewModel.initializer),
                viewModelDependencies);
    }
}
