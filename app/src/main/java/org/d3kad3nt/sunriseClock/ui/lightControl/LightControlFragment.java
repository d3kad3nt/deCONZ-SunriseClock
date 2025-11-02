package org.d3kad3nt.sunriseClock.ui.lightControl;

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
import com.google.android.material.slider.Slider;
import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.backend.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.databinding.LightControlFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.util.BaseFragment;
import org.d3kad3nt.sunriseClock.ui.util.MenuHandler;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class LightControlFragment extends BaseFragment<LightControlFragmentBinding, LightControlViewModel>
        implements MenuHandler {

    @Override
    protected LightControlFragmentBinding getViewBinding(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        return LightControlFragmentBinding.inflate(inflater, container, false);
    }

    @Override
    protected Class<LightControlViewModel> getViewModelClass() {
        return LightControlViewModel.class;
    }

    @Override
    protected void bindVars(final LightControlFragmentBinding binding) {
        binding.setViewModel(viewModel);
        binding.setBrightnessSliderTouchListener(new BrightnessSliderTouchListener());
    }

    @Override
    protected LifecycleOwner getLifecycleOwner() {
        return getViewLifecycleOwner();
    }

    @Override
    protected ViewModelProvider getViewModelProvider() {
        long lightId = LightControlFragmentArgs.fromBundle(requireArguments()).getLightId(); // id from navigation

        LogUtil.setPrefix("LightId %d: ", lightId);

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
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_nested_light_control);

        // Initialize viewModel with light id and inject the light repository.
        // By injecting the repository, the viewModel no longer needs the Application or Context.
        MutableCreationExtras viewModelDependencies = new MutableCreationExtras();
        viewModelDependencies.set(
                LightControlViewModel.LIGHT_REPOSITORY_KEY, LightRepository.getInstance(requireContext()));
        viewModelDependencies.set(LightControlViewModel.LIGHT_ID_KEY, lightId);

        // Use custom factory to initialize the viewModel (instead of using new
        // ViewModelProvider(this).get(LightControlViewModel.class)).
        // For viewModel older than 2.5.0 ViewModelProvider.Factory had to be extended.
        return new ViewModelProvider(
                backStackEntry.getViewModelStore(),
                ViewModelProvider.Factory.from(LightControlViewModel.initializer),
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
        if (menuItem.getItemId() == R.id.menu_light_details_refresh) {
            LogUtil.d("User requested a light refresh by clicking the toolbar menu option.");
            viewModel.refreshLight();
            return true;
        } else if (menuItem.getItemId() == R.id.menu_light_details_info) {
            LogUtil.d("User requested to show light info screen by clicking the toolbar menu option.");
            Navigation.findNavController(binding.getRoot())
                    .navigate(LightControlFragmentDirections.actionLightDetailToLightDetailInfoDialogFragment());
            return true;
        } else if (menuItem.getItemId() == R.id.menu_light_details_name_edit) {
            LogUtil.d("User requested to show light name edit screen by clicking the toolbar menu option.");
            Navigation.findNavController(binding.getRoot())
                    .navigate(LightControlFragmentDirections.actionLightDetailToLightDetailNameEditDialogFragment());
            return true;
        }
        return false;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_light_details;
    }

    // An OnChangeListener would report every single change, even when still dragging.
    // OnSliderTouchListener reports only once, after the slider touch is released.
    public class BrightnessSliderTouchListener implements Slider.OnSliderTouchListener {

        @Override
        public void onStartTrackingTouch(@NonNull final Slider slider) {}

        @Override
        public void onStopTrackingTouch(@NonNull final Slider slider) {
            viewModel.setLightBrightness((int) slider.getValue());
        }
    }
}
