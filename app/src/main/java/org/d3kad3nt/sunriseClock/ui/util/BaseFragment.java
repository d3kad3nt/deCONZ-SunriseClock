package org.d3kad3nt.sunriseClock.ui.util;

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
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;

import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.databinding.CommonToolbarBinding;
import org.d3kad3nt.sunriseClock.util.LogUtil;

import java.util.Optional;

public abstract class BaseFragment<DataBindingT extends ViewDataBinding, ViewModelT extends ViewModel> extends Fragment
        implements MenuProvider {

    protected DataBindingT binding;

    protected ViewModelT viewModel;
    private Optional<MenuHandler> menuHandler = Optional.empty();
    private CommonToolbarBinding commonToolbarBinding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        LogUtil.i("Create fragment view: %s.", this.getClass().getSimpleName());

        binding = getViewBinding(inflater, container, savedInstanceState);
        commonToolbarBinding = CommonToolbarBinding.bind(binding.getRoot());

        this.menuHandler = Optional.ofNullable(bindMenu());

        if (menuHandler.isPresent()) {
            // Initialize the options menu (toolbar menu).
            commonToolbarBinding.toolbar.addMenuProvider(this, getViewLifecycleOwner());
        }

        viewModel = getViewModelProvider().get(getViewModelClass());

        return binding.getRoot();
    }

    /**
     * Overwrite this Method, if a custom Menu should be defined
     */
    @Nullable
    protected MenuHandler bindMenu() {
        return null;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(view);
        // In some cases, you might need to define multiple top-level destinations instead of using
        // the default start destination.
        // Using a BottomNavigationView is a common use case for this, where you may have sibling
        // screens that are not hierarchically related to each other and may each have their own set
        // of related destinations.
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.lightsList, R.id.endpointsList, R.id.mainSettingsFragment).build();
        NavigationUI.setupWithNavController(commonToolbarBinding.toolbar, navController, appBarConfiguration);

        bindVars(binding);

        binding.setLifecycleOwner(getLifecycleOwner());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    protected MaterialToolbar getToolbar() {
        return commonToolbarBinding.toolbar;
    }

    @Override
    public void onCreateMenu(@NonNull final Menu menu, @NonNull final MenuInflater menuInflater) {
        if (menuHandler.isPresent()) {
            // XML menu resources do not support view or data binding: We have to use the R class.
            LogUtil.d("Adding menu options to the toolbar.");
            menuInflater.inflate(menuHandler.get().getMenuId(), menu);
        }
    }

    @Override
    public boolean onMenuItemSelected(@NonNull final MenuItem menuItem) {
        if (menuHandler.isPresent()) {
            return menuHandler.get().onMenuClicked(menuItem);
        }
        return false;
    }

    protected abstract DataBindingT getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    protected abstract Class<ViewModelT> getViewModelClass();

    /**
     * Can be used to set values in the databinding class.
     *
     * <p>Typically, the developer will be able to call the subclass's set method directly.
     * For example, if there is a variable x in the Binding, a setX method will be generated.
     */
    protected abstract void bindVars(DataBindingT binding);

    /**
     * Must return the {@link LifecycleOwner} that should be used for observing changes of LiveData
     * in this binding.
     *
     * <p>If a LiveData is in one of the binding expressions and no LifecycleOwner is set, the LiveData will not be
     * observed and updates to it will not be propagated to the UI. Note from Google: When subscribing to
     * lifecycle-aware components such as LiveData, never use viewLifecycleOwner as the LifecycleOwner in a
     * DialogFragment that uses Dialog objects, instead, use the the DialogFragment itself, or, if you're using Jetpack
     * Navigation, use the NavBackstackEntry.
     */
    protected abstract LifecycleOwner getLifecycleOwner();

    /**
     * Creates ViewModelProvider, used to create VM instances and retain them in the ViewModelStore of the given
     * ViewModelStoreOwner.
     */
    protected abstract ViewModelProvider getViewModelProvider();
}
