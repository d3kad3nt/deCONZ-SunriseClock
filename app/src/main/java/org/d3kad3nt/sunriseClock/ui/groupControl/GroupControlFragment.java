package org.d3kad3nt.sunriseClock.ui.groupControl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.MutableCreationExtras;
import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.backend.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.databinding.GroupControlFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.util.BaseFragment;
import org.d3kad3nt.sunriseClock.ui.util.MenuHandler;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class GroupControlFragment extends BaseFragment<GroupControlFragmentBinding, GroupControlViewModel>
        implements MenuHandler {

    @Override
    protected GroupControlFragmentBinding getViewBinding(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return GroupControlFragmentBinding.inflate(inflater, container, false);
    }

    @Override
    protected Class<GroupControlViewModel> getViewModelClass() {
        return GroupControlViewModel.class;
    }

    @Override
    protected void bindVars(GroupControlFragmentBinding binding) {
        binding.setViewModel(viewModel);
    }

    @Override
    protected LifecycleOwner getLifecycleOwner() {
        return getViewLifecycleOwner();
    }

    @Override
    protected ViewModelProvider getViewModelProvider() {
        long groupId = GroupControlFragmentArgs.fromBundle(requireArguments()).getGroupId(); // id from navigation

        LogUtil.setPrefix("GroupId %d: ", groupId);

        // Initialize viewModel with group id and inject the light repository.
        // By injecting the repository, the viewModel no longer needs the Application or Context.
        MutableCreationExtras viewModelDependencies = new MutableCreationExtras();
        viewModelDependencies.set(
                GroupControlViewModel.LIGHT_REPOSITORY_KEY, LightRepository.getInstance(requireContext()));
        viewModelDependencies.set(GroupControlViewModel.GROUP_ID_KEY, groupId);

        return new ViewModelProvider(
                this.getViewModelStore(),
                ViewModelProvider.Factory.from(GroupControlViewModel.initializer),
                viewModelDependencies);
    }

    @Nullable
    @Override
    protected MenuHandler bindMenu() {
        return this;
    }

    @Override
    public boolean onMenuClicked(@NonNull final MenuItem menuItem) {
        // The SwipeRefreshLayout does not provide accessibility events.
        // Instead, a menu item should be provided to allow refresh of the content wherever this
        // gesture is used.
        if (menuItem.getItemId() == R.id.menu_group_details_refresh) {
            LogUtil.d("User requested a group refresh by clicking the toolbar menu option.");
            viewModel.refreshGroup();
            return true;
        }
        return false;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_group_details;
    }
}
