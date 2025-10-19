package org.d3kad3nt.sunriseClock.ui.group.groupDetail;

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
import org.d3kad3nt.sunriseClock.databinding.GroupDetailFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.util.BaseFragment;
import org.d3kad3nt.sunriseClock.ui.util.MenuHandler;

public class GroupDetailFragment extends BaseFragment<GroupDetailFragmentBinding, GroupDetailViewModel> {

    @Nullable
    @Override
    protected MenuHandler bindMenu() {
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        long groupId = GroupDetailFragmentArgs.fromBundle(requireArguments()).getGroupId();
        viewModel.loadGroup(groupId);

        binding.swipeRefreshLayout.setOnRefreshListener(() -> viewModel.refreshGroup());
    }

    @Override
    protected GroupDetailFragmentBinding getViewBinding(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return GroupDetailFragmentBinding.inflate(inflater, container, false);
    }

    @Override
    protected Class<GroupDetailViewModel> getViewModelClass() {
        return GroupDetailViewModel.class;
    }

    @Override
    protected void bindVars(GroupDetailFragmentBinding binding) {
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
                GroupDetailViewModel.LIGHT_REPOSITORY_KEY, LightRepository.getInstance(requireContext()));

        return new ViewModelProvider(
                this.getViewModelStore(),
                ViewModelProvider.Factory.from(GroupDetailViewModel.initializer),
                viewModelDependencies);
    }
}
