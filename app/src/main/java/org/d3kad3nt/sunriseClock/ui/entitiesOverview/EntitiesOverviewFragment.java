package org.d3kad3nt.sunriseClock.ui.entitiesOverview;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ConcatAdapter;
import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.backend.data.model.resource.Status;
import org.d3kad3nt.sunriseClock.backend.data.repository.LightRepository;
import org.d3kad3nt.sunriseClock.backend.data.repository.SettingsRepository;
import org.d3kad3nt.sunriseClock.databinding.EntitiesOverviewFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.util.BaseFragment;
import org.d3kad3nt.sunriseClock.ui.util.MenuHandler;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class EntitiesOverviewFragment extends BaseFragment<EntitiesOverviewFragmentBinding, EntitiesOverviewViewModel>
        implements EntitiesOverviewListAdapterLight.ClickListeners,
                EntitiesOverviewListAdapterGroup.ClickListeners,
                MenuHandler {

    @Override
    protected EntitiesOverviewFragmentBinding getViewBinding(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        return EntitiesOverviewFragmentBinding.inflate(inflater, container, false);
    }

    @Override
    protected Class<EntitiesOverviewViewModel> getViewModelClass() {
        return EntitiesOverviewViewModel.class;
    }

    @Override
    protected void bindVars(final EntitiesOverviewFragmentBinding binding) {
        binding.setViewModel(viewModel);

        EntitiesOverviewListAdapterHeader lightsHeaderAdapter =
                new EntitiesOverviewListAdapterHeader(getString(R.string.lights));
        EntitiesOverviewListAdapterLight lightsAdapter = new EntitiesOverviewListAdapterLight(this);
        EntitiesOverviewListAdapterHeader groupsHeaderAdapter =
                new EntitiesOverviewListAdapterHeader(getString(R.string.groups));
        EntitiesOverviewListAdapterGroup groupsAdapter = new EntitiesOverviewListAdapterGroup(this);

        // Display the contents of multiple adapters sequentially.
        ConcatAdapter concatAdapter =
                new ConcatAdapter(lightsHeaderAdapter, lightsAdapter, groupsHeaderAdapter, groupsAdapter);
        binding.recyclerView.setAdapter(concatAdapter);

        viewModel.getLights().observe(getLifecycleOwner(), listResource -> {
            if (listResource.getStatus().equals(Status.SUCCESS) && listResource.getData() != null) {
                LogUtil.i("Lights in list updated - LiveData onChanged triggered.");
                lightsAdapter.submitList(listResource.getData());
            }
        });

        viewModel.getGroups().observe(getLifecycleOwner(), listResource -> {
            if (listResource.getStatus().equals(Status.SUCCESS) && listResource.getData() != null) {
                LogUtil.i("Groups in list updated - LiveData onChanged triggered.");
                groupsAdapter.submitList(listResource.getData());
            }
        });
    }

    @Override
    protected LifecycleOwner getLifecycleOwner() {
        return getViewLifecycleOwner();
    }

    @Override
    protected ViewModelProvider getViewModelProvider() {
        // Initialize viewModel by injecting the light and settings repository.
        // By injecting the repository, the viewModel no longer needs the Application or Context.
        MutableCreationExtras viewModelDependencies = new MutableCreationExtras();
        viewModelDependencies.set(
                EntitiesOverviewViewModel.LIGHT_REPOSITORY_KEY, LightRepository.getInstance(requireContext()));
        viewModelDependencies.set(
                EntitiesOverviewViewModel.SETTINGS_REPOSITORY_KEY, SettingsRepository.getInstance(requireContext()));

        // Use custom factory to initialize the viewModel (instead of using new
        // ViewModelProvider(this).get(EntitiesOverviewViewModel.class)).
        // For viewModel older than 2.5.0 ViewModelProvider.Factory had to be extended.
        return new ViewModelProvider(
                this.getViewModelStore(),
                ViewModelProvider.Factory.from(EntitiesOverviewViewModel.initializer),
                viewModelDependencies);
    }

    @Override
    public void onLightCardClick(final View view, final long lightId, final String lightName) {
        LogUtil.d("Navigate to light detail view for light %s (id %d)", lightName, lightId);
        Navigation.findNavController(view)
                .navigate(EntitiesOverviewFragmentDirections.actionBottomnavEntitiesToNavGraphLightDetail(
                        lightId, lightName));
    }

    @Override
    public void onGroupCardClick(final View view, final long groupId, final String groupName) {
        LogUtil.d("Navigate to group detail view for group %s (id %d)", groupName, groupId);
        Navigation.findNavController(view)
                .navigate(EntitiesOverviewFragmentDirections.actionBottomnavEntitiesToGroupDetail(groupId, groupName));
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
        if (menuItem.getItemId() == R.id.menu_entities_refresh) {
            LogUtil.d("User requested refresh of all entities by clicking the toolbar menu option.");
            viewModel.refreshEntities();
            return true;
        }
        return false;
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_entities;
    }
}
