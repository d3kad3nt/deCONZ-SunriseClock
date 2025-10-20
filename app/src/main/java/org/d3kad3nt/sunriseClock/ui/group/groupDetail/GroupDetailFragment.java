package org.d3kad3nt.sunriseClock.ui.group.groupDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.MutableCreationExtras;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.backend.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.databinding.GroupDetailFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.light.lightDetail.LightDetailFragmentArgs;
import org.d3kad3nt.sunriseClock.ui.light.lightDetail.LightDetailFragmentDirections;
import org.d3kad3nt.sunriseClock.ui.light.lightDetail.LightDetailViewModel;
import org.d3kad3nt.sunriseClock.ui.util.BaseFragment;
import org.d3kad3nt.sunriseClock.ui.util.MenuHandler;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class GroupDetailFragment extends BaseFragment<GroupDetailFragmentBinding, GroupDetailViewModel>
        implements MenuHandler {

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
        long groupId = GroupDetailFragmentArgs.fromBundle(requireArguments()).getGroupId(); // id from navigation

        LogUtil.setPrefix("GroupId %d: ", groupId);

        // Initialize viewModel with group id and inject the light repository.
        // By injecting the repository, the viewModel no longer needs the Application or Context.
        MutableCreationExtras viewModelDependencies = new MutableCreationExtras();
        viewModelDependencies.set(
                GroupDetailViewModel.LIGHT_REPOSITORY_KEY, LightRepository.getInstance(requireContext()));
        viewModelDependencies.set(GroupDetailViewModel.GROUP_ID_KEY, groupId);

        return new ViewModelProvider(
                this.getViewModelStore(),
                ViewModelProvider.Factory.from(GroupDetailViewModel.initializer),
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
