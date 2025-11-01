package org.d3kad3nt.sunriseClock.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
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
import org.d3kad3nt.sunriseClock.databinding.HomeFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.util.BaseFragment;
import org.d3kad3nt.sunriseClock.ui.util.MenuHandler;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class HomeFragment extends BaseFragment<HomeFragmentBinding, HomeViewModel>
        implements HomeListAdapter.ClickListeners, MenuHandler {

    private final LightsState lightsState = new LightsState();
    private HomeListAdapter adapter;

    @Override
    protected HomeFragmentBinding getViewBinding(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        return HomeFragmentBinding.inflate(inflater, container, false);
    }

    @Override
    protected Class<HomeViewModel> getViewModelClass() {
        return HomeViewModel.class;
    }

    @Override
    protected void bindVars(final HomeFragmentBinding binding) {
        binding.setViewModel(viewModel);
        binding.setLightsState(lightsState);

        adapter = new HomeListAdapter(this);
        binding.recyclerView.setAdapter(adapter);
        viewModel.getGroupsWithLights().observe(getLifecycleOwner(), new Observer<>() {
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
    }

    @Override
    protected LifecycleOwner getLifecycleOwner() {
        return getViewLifecycleOwner();
    }

    @Override
    protected ViewModelProvider getViewModelProvider() {
        return new ViewModelProvider(this);
    }

    @Override
    public void onLightCardClick(View view, final long lightId, final String lightName) {
        LogUtil.d("Navigate to light detail view for light %s (id %d)", lightName, lightId);
        Navigation.findNavController(view)
                .navigate(HomeFragmentDirections.actionLightsToLightDetail(lightId, lightName));
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
        LogUtil.d("Navigate to group detail view for group %s (id %d)", groupName, groupId);
        Navigation.findNavController(view)
                .navigate(HomeFragmentDirections.actionLightsToGroupDetail(groupId, groupName));
    }

    @Override
    public void onGroupSwitchCheckedChange(final long groupId, final boolean state) {
        viewModel.setGroupOnState(groupId, state);
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
        if (menuItem.getItemId() == R.id.menu_lights_refresh) {
            LogUtil.d("User requested refresh of all groups and lights by clicking the toolbar menu option.");
            viewModel.refreshGroupsWithLights();
            return true;
        }
        return false;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_lights;
    }
}
