package org.d3kad3nt.sunriseClock.ui.entity;

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
import org.d3kad3nt.sunriseClock.databinding.EntitiesFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.util.BaseFragment;
import org.d3kad3nt.sunriseClock.ui.util.MenuHandler;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class EntitiesFragment extends BaseFragment<EntitiesFragmentBinding, EntitiesViewModel>
        implements EntitiesListAdapterLight.ClickListeners, EntitiesListAdapterGroup.ClickListeners, MenuHandler {

    @Override
    protected EntitiesFragmentBinding getViewBinding(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        return EntitiesFragmentBinding.inflate(inflater, container, false);
    }

    @Override
    protected Class<EntitiesViewModel> getViewModelClass() {
        return EntitiesViewModel.class;
    }

    @Override
    protected void bindVars(final EntitiesFragmentBinding binding) {
        binding.setViewModel(viewModel);

        EntitiesListAdapterHeader lightsHeaderAdapter = new EntitiesListAdapterHeader(getString(R.string.lights));
        EntitiesListAdapterLight lightsAdapter = new EntitiesListAdapterLight(this);
        EntitiesListAdapterHeader groupsHeaderAdapter = new EntitiesListAdapterHeader(getString(R.string.groups));
        EntitiesListAdapterGroup groupsAdapter = new EntitiesListAdapterGroup(this);

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
                EntitiesViewModel.LIGHT_REPOSITORY_KEY, LightRepository.getInstance(requireContext()));
        viewModelDependencies.set(
                EntitiesViewModel.SETTINGS_REPOSITORY_KEY, SettingsRepository.getInstance(requireContext()));

        // Use custom factory to initialize the viewModel (instead of using new
        // ViewModelProvider(this).get(EntitiesViewModel.class)).
        // For viewModel older than 2.5.0 ViewModelProvider.Factory had to be extended.
        return new ViewModelProvider(
                this.getViewModelStore(),
                ViewModelProvider.Factory.from(EntitiesViewModel.initializer),
                viewModelDependencies);
    }

    @Override
    public void onLightCardClick(final View view, final long lightId, final String lightName) {
        LogUtil.d("Navigate to light detail view for light %s (id %d)", lightName, lightId);
        Navigation.findNavController(view)
                .navigate(EntitiesFragmentDirections.actionBottomnavEntitiesToNavGraphLightDetail(lightId, lightName));
    }

    @Override
    public void onGroupCardClick(final View view, final long groupId, final String groupName) {
        LogUtil.d("Navigate to group detail view for group %s (id %d)", groupName, groupId);
        Navigation.findNavController(view)
                .navigate(EntitiesFragmentDirections.actionBottomnavEntitiesToNavGraphLightGroupDetail(
                        groupId, groupName));
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
