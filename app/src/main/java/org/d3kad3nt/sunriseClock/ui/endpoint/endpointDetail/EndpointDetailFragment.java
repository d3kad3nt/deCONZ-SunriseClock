package org.d3kad3nt.sunriseClock.ui.endpoint.endpointDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import org.d3kad3nt.sunriseClock.backend.data.repository.EndpointRepository;
import org.d3kad3nt.sunriseClock.backend.data.repository.SettingsRepository;
import org.d3kad3nt.sunriseClock.databinding.EndpointDetailFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.util.BaseFragment;
import org.d3kad3nt.sunriseClock.ui.util.MenuHandler;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class EndpointDetailFragment extends BaseFragment<EndpointDetailFragmentBinding, EndpointDetailViewModel>
        implements MenuHandler {

    @Override
    protected EndpointDetailFragmentBinding getViewBinding(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        return EndpointDetailFragmentBinding.inflate(inflater, container, false);
    }

    @Override
    protected Class<EndpointDetailViewModel> getViewModelClass() {
        return EndpointDetailViewModel.class;
    }

    @Override
    protected void bindVars(final EndpointDetailFragmentBinding binding) {
        binding.setViewModel(viewModel);
    }

    @Override
    protected LifecycleOwner getLifecycleOwner() {
        return getViewLifecycleOwner();
    }

    @Override
    protected ViewModelProvider getViewModelProvider() {
        long endpointID =
                EndpointDetailFragmentArgs.fromBundle(requireArguments()).getEndpointID(); // id from navigation

        LogUtil.setPrefix("EndpointId %d: ", endpointID);

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
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_graph_endpoint_detail);

        // Initialize viewModel with endpoint id and inject the endpoint and settings repository.
        // By injecting the repository, the viewModel no longer needs the Application or Context.
        MutableCreationExtras viewModelDependencies = new MutableCreationExtras();
        viewModelDependencies.set(
                EndpointDetailViewModel.ENDPOINT_REPOSITORY_KEY, EndpointRepository.getInstance(requireContext()));
        viewModelDependencies.set(
                EndpointDetailViewModel.SETTINGS_REPOSITORY_KEY, SettingsRepository.getInstance(requireContext()));
        viewModelDependencies.set(EndpointDetailViewModel.ENDPOINT_ID_KEY, endpointID);

        // Use custom factory to initialize the viewModel (instead of using new
        // ViewModelProvider(this).get(EndpointDetailViewModel.class)).
        // For viewModel older than 2.5.0 ViewModelProvider.Factory had to be extended.
        return new ViewModelProvider(
                backStackEntry.getViewModelStore(),
                ViewModelProvider.Factory.from(EndpointDetailViewModel.initializer),
                viewModelDependencies);
    }

    @Nullable
    @Override
    protected MenuHandler bindMenu() {
        return this;
    }

    @Override
    public boolean onMenuClicked(@NonNull final MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.menu_endpoint_details_info) {
            LogUtil.d("User requested to show endpoint info screen by clicking the toolbar menu option.");
            Navigation.findNavController(binding.getRoot())
                    .navigate(
                            EndpointDetailFragmentDirections.actionEndpointDetailToEndpointDetailInfoDialogFragment());
            return true;
        } else if (menuItem.getItemId() == R.id.menu_endpoint_details_name_edit) {
            LogUtil.d("User requested to show endpoint name edit screen by clicking the toolbar menu option.");
            Navigation.findNavController(binding.getRoot())
                    .navigate(
                            EndpointDetailFragmentDirections
                                    .actionEndpointDetailToEndpointDetailNameEditDialogFragment());
            return true;
        }
        return false;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_endpoint_details;
    }
}
