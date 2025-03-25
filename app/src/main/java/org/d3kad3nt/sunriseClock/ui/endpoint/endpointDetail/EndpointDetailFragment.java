package org.d3kad3nt.sunriseClock.ui.endpoint.endpointDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.MutableCreationExtras;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.data.repository.EndpointRepository;
import org.d3kad3nt.sunriseClock.data.repository.SettingsRepository;
import org.d3kad3nt.sunriseClock.databinding.EndpointDetailFragmentBinding;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class EndpointDetailFragment extends Fragment implements MenuProvider {

    private EndpointDetailFragmentBinding binding;
    private EndpointDetailViewModel viewModel;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        long endpointID = EndpointDetailFragmentArgs.fromBundle(requireArguments())
                .getEndpointID(); // id from navigation

        LogUtil.setPrefix("EndpointID %d: ", endpointID);
        LogUtil.d("Show endpoint detail view");

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
        NavBackStackEntry backStackEntry =
                navController.getBackStackEntry(R.id.nav_graph_endpoint_detail);

        // Initialize viewModel with endpoint id and inject the endpoint and settings repository.
        // By injecting the repository, the viewModel no longer needs the Application or Context.
        MutableCreationExtras viewModelDependencies = new MutableCreationExtras();
        viewModelDependencies.set(
                EndpointDetailViewModel.ENDPOINT_REPOSITORY_KEY,
                EndpointRepository.getInstance(requireContext()));
        viewModelDependencies.set(
                EndpointDetailViewModel.SETTINGS_REPOSITORY_KEY,
                SettingsRepository.getInstance(requireContext()));
        viewModelDependencies.set(EndpointDetailViewModel.ENDPOINT_ID_KEY, endpointID);

        // Use custom factory to initialize the viewModel (instead of using new
        // ViewModelProvider(this).get(EndpointDetailViewModel.class)).
        // For viewModel older than 2.5.0 ViewModelProvider.Factory had to be extended.
        viewModel = new ViewModelProvider(
                        backStackEntry.getViewModelStore(),
                        ViewModelProvider.Factory.from(EndpointDetailViewModel.initializer),
                        viewModelDependencies)
                .get(EndpointDetailViewModel.class);

        binding = EndpointDetailFragmentBinding.inflate(inflater, container, false);

        // Initialize the options menu (toolbar menu).
        binding.endpointDetailsToolbar.addMenuProvider(this, getViewLifecycleOwner());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(view);

        // In some cases, you might need to define multiple top-level destinations instead of using
        // the default start
        // destination.
        // Using a BottomNavigationView is a common use case for this, where you may have sibling
        // screens that are
        // not hierarchically related to each other and may each have their own set of related
        // destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.lightsList, R.id.endpointsList, R.id.mainSettingsFragment)
                .build();

        NavigationUI.setupWithNavController(
                binding.endpointDetailsToolbar, navController, appBarConfiguration);

        binding.setViewModel(viewModel);
        // Specify the fragment view as the lifecycle owner of the binding. This is used so that the
        // binding can observe LiveData updates.
        binding.setLifecycleOwner(getViewLifecycleOwner());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateMenu(@NonNull final Menu menu, @NonNull final MenuInflater menuInflater) {
        // XML menu resources do not support view or data binding: We have to use the R class.
        LogUtil.d("Adding menu options to the toolbar.");
        menuInflater.inflate(R.menu.menu_endpoint_details, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull final MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.menu_endpoint_details_info) {
            LogUtil.d(
                    "User requested to show endpoint info screen by clicking the toolbar menu option.");
            Navigation.findNavController(binding.getRoot())
                    .navigate(
                            EndpointDetailFragmentDirections
                                    .actionEndpointDetailToEndpointDetailInfoDialogFragment());
            return true;
        } else if (menuItem.getItemId() == R.id.menu_endpoint_details_name_edit) {
            LogUtil.d(
                    "User requested to show endpoint name edit screen by clicking the toolbar menu option.");
            Navigation.findNavController(binding.getRoot())
                    .navigate(
                            EndpointDetailFragmentDirections
                                    .actionEndpointDetailToEndpointDetailNameEditDialogFragment());
            return true;
        } else {
            return false;
        }
    }
}
