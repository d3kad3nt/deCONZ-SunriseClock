package org.d3kad3nt.sunriseClock.ui.util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.d3kad3nt.sunriseClock.R;

// There are at least two ways to create a custom dialog in Android:
// 1.: Override onCreateDialog() and use the MaterialAlertDialogBuilder to define the layout
// (including buttons and onClickListeners) programmatically.
// 2.: Override onCreateView() and inflate a fully custom XML layout.
// We chose the second method because this gives us full control over the layout and allows
// databinding.
// By treating the DialogFragment as a regular fragment, the handling is almost the same.
public abstract class BaseDialogFragment<
                DataBindingT extends ViewDataBinding, ViewModelT extends ViewModel>
        extends DialogFragment {

    protected DataBindingT binding;
    protected ViewModelT viewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        binding = getViewBinding();
        viewModel = getViewModelProvider().get(getViewModelClass());

        // Google: The view returned by onCreateView() is automatically added to the dialog. In most
        // cases, this means that we don't need to override onCreateDialog(), as the default empty
        // dialog is populated with our view.
        return binding.getRoot();
    }

    // onViewCreated() is only called because we've overridden onCreateView() and provided a
    // non-null view.
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindVars();

        binding.setLifecycleOwner(observeData());
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We do not use the MaterialAlertDialogBuilder: We have to take care of styling the dialog
        // ourselves. Otherwise, it would be in AppCompat style and would not fit into our app.
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    protected abstract DataBindingT getViewBinding();

    protected abstract Class<ViewModelT> getViewModelClass();

    /**
     * Can be used to set values in the databinding class.
     *
     * <p>The binding class is available under {@link #binding}. Typically, the developer will be
     * able to call the subclass's set method directly. For example, if there is a variable x in the
     * Binding, a setX method will be generated.
     */
    protected abstract void bindVars();

    /**
     * Must return the {@link androidx.lifecycle.LifecycleOwner} that should be used for observing
     * changes of LiveData in this binding.
     *
     * <p>If a LiveData is in one of the binding expressions and no LifecycleOwner is set, the
     * LiveData will not be observed and updates to it will not be propagated to the UI. Note from
     * Google: When subscribing to lifecycle-aware components such as LiveData, never use
     * viewLifecycleOwner as the LifecycleOwner in a DialogFragment that uses Dialog objects,
     * instead, use the the DialogFragment itself, or, if you're using Jetpack Navigation, use the
     * NavBackstackEntry.
     */
    protected abstract LifecycleOwner observeData();

    /**
     * Creates ViewModelProvider, used to create VM instances and retain them in the ViewModelStore
     * of the given ViewModelStoreOwner.
     */
    protected abstract ViewModelProvider getViewModelProvider();
}
