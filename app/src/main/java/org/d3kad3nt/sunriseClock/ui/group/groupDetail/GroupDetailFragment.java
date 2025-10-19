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
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.backend.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.databinding.GroupDetailFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.light.lightDetail.LightDetailFragmentArgs;
import org.d3kad3nt.sunriseClock.ui.light.lightDetail.LightDetailViewModel;
import org.d3kad3nt.sunriseClock.ui.util.BaseFragment;
import org.d3kad3nt.sunriseClock.ui.util.MenuHandler;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class GroupDetailFragment extends BaseFragment<GroupDetailFragmentBinding, GroupDetailViewModel> {

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

        // We are using a nested navigation graph. From
        // https://developer.android.com/guide/navigation/use-graph/programmatic#share_ui-related_data_between_destinations_with_viewmodel:
        // The Navigation back stack stores a NavBackStackEntry not only for each individual
        // destination, but also for each parent navigation graph that contains the individual
        // destination.
        // This allows you to retrieve a NavBackStackEntry that is scoped to a navigation graph.
        // A navigation graph-scoped NavBackStackEntry provides a way to create a ViewModel that's
        // scoped to a navigation graph, enabling you to share UI-related data between the graph's
        // destinations.
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_graph_group_detail);

        // Initialize viewModel with light id and inject the light repository.
        // By injecting the repository, the viewModel no longer needs the Application or Context.
        MutableCreationExtras viewModelDependencies = new MutableCreationExtras();
        viewModelDependencies.set(
                GroupDetailViewModel.LIGHT_REPOSITORY_KEY, LightRepository.getInstance(requireContext()));
        viewModelDependencies.set(GroupDetailViewModel.GROUP_ID_KEY, groupId);

        return new ViewModelProvider(
                backStackEntry.getViewModelStore(),
                ViewModelProvider.Factory.from(GroupDetailViewModel.initializer),
                viewModelDependencies);
    }
}
