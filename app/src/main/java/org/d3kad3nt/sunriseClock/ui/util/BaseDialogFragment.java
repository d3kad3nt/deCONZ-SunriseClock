package org.d3kad3nt.sunriseClock.ui.util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import org.d3kad3nt.sunriseClock.R;

public abstract class BaseDialogFragment<DataBindingT extends ViewDataBinding, ViewModelT extends ViewModel> extends DialogFragment {

    protected DataBindingT binding;
    protected abstract DataBindingT getViewBinding();

    protected ViewModelT viewModel;
    protected abstract Class<ViewModelT> getViewModelClass();

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        binding = getViewBinding();

        // NavBackStackEntry and viewModel scoped to our nested nav graph (containing all light detail screens).
        NavController navController = NavHostFragment.findNavController(this);
        // Todo: Generic creation of viewModel (instead of hardcoded resource).
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_graph_light_detail);
        viewModel = new ViewModelProvider(backStackEntry).get(getViewModelClass());

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindVars();
        observeData();
    }

    /**
     * Can be used to set values in the databinding class.
     * <p>
     * The binding class is available under {@link #binding}.
     * Typically, the developer will be able to call the subclass's set method directly.
     * For example, if there is a variable x in the Binding, a setX method will be generated.
     */
    protected abstract void bindVars();

    /**
     * Can be used to set the {@link androidx.lifecycle.LifecycleOwner} that should be used for observing changes of LiveData in this binding.
     * <p>
     * If a LiveData is in one of the binding expressions and no LifecycleOwner is set,
     * the LiveData will not be observed and updates to it will not be propagated to the UI.
     */
    protected abstract void observeData();

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Todo: Explain custom style.
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }
}
