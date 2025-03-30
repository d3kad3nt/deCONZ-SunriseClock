package org.d3kad3nt.sunriseClock.ui.light;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.backend.data.model.ListItem;
import org.d3kad3nt.sunriseClock.backend.data.model.group.UIGroup;
import org.d3kad3nt.sunriseClock.backend.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.Status;
import org.d3kad3nt.sunriseClock.databinding.LightsFragmentBinding;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class LightsFragment extends Fragment implements LightsListAdapter.ClickListeners, MenuProvider {

    private final LightsState lightsState = new LightsState();
    private LightsFragmentBinding binding;
    private LightsViewModel viewModel;
    private LightsListAdapter adapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.d("Show light list view");
        viewModel = new ViewModelProvider(requireActivity()).get(LightsViewModel.class);

        binding = LightsFragmentBinding.inflate(inflater, container, false);

        binding.setLightsState(lightsState);

        adapter = new LightsListAdapter(this);
        binding.recyclerView.setAdapter(adapter);

        viewModel.getGroupsWithLights().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(final Resource<Map<UIGroup, List<UILight>>> mapResource) {
                if (mapResource.getStatus().equals(Status.SUCCESS) && mapResource.getData() != null) {
                    LogUtil.i("Lights in list updated");
                    lightsState.clearError();

                    // Use comparable interface in UIGroup (and then UIList) to sort the output.
                    List<ListItem> flatList = mapResource.getData().entrySet().stream()
                            .sorted(Map.Entry.comparingByKey())
                            .flatMap(entry -> Stream.concat(
                                    Stream.of(entry.getKey()),
                                    entry.getValue().stream().sorted()))
                            .collect(Collectors.toUnmodifiableList());

                    adapter.submitList(flatList);
                } else if (mapResource.getStatus().equals(Status.ERROR)) {
                    LogUtil.i("No Lights found");
                    lightsState.setError(getResources().getString(R.string.noLights_title), mapResource.getMessage());
                    adapter.submitList(List.of());
                }
            }
        });

        // Initialize the options menu (toolbar menu).
        binding.lightsToolbar.addMenuProvider(this, getViewLifecycleOwner());

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
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.lightsList, R.id.endpointsList, R.id.mainSettingsFragment).build();

        NavigationUI.setupWithNavController(binding.lightsToolbar, navController, appBarConfiguration);

        binding.setViewModel(viewModel);
        // Specify the fragment view as the lifecycle owner of the binding. This is used so that the
        // binding can
        // observe LiveData updates.
        binding.setLifecycleOwner(getViewLifecycleOwner());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onLightCardClick(View view, final long lightId, final String lightName) {
        LogUtil.d("Navigate to light detail view for light %s (Id %d)", lightName, lightId);
        Navigation.findNavController(view)
                .navigate(LightsFragmentDirections.actionLightsToLightDetail(lightId, lightName));
    }

    @Override
    public void onLightSwitchCheckedChange(final long lightId, final boolean state) {
        viewModel.setLightOnState(lightId, state);
    }

    @Override
    public void onLightSliderTouch(
            final long lightId, @IntRange(from = 0, to = 100) final int brightness, final boolean state) {
        viewModel.setLightBrightness(lightId, brightness, state);
    }

    @Override
    public void onGroupCardClick(final View view, final long groupId, final String groupName) {
        // Todo: Implement group detail screen.
        LogUtil.d("Group card clicked.");
    }

    @Override
    public void onCreateMenu(@NonNull final Menu menu, @NonNull final MenuInflater menuInflater) {
        // XML menu resources do not support view or data binding: We have to use the R class.
        LogUtil.d("Adding menu options to the toolbar.");
        menuInflater.inflate(R.menu.menu_lights, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull final MenuItem menuItem) {
        // The SwipeRefreshLayout does not provide accessibility events.
        // Instead, a menu item should be provided to allow refresh of the content wherever this
        // gesture is used.
        if (menuItem.getItemId() == R.id.menu_lights_refresh) {
            LogUtil.d("User requested refresh of all lights by clicking the toolbar menu option.");
            viewModel.refreshLights();
            return true;
        }
        return false;
    }
}
