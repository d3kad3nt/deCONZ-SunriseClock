package org.d3kad3nt.sunriseClock.ui.lightControl;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.databinding.LightControlInfoDialogFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.util.BaseDialogFragment;
import org.d3kad3nt.sunriseClock.ui.util.DialogOkClickListener;

public class LightControlInfoDialogFragment
        extends BaseDialogFragment<LightControlInfoDialogFragmentBinding, LightControlViewModel>
        implements DialogOkClickListener {

    @Override
    protected LightControlInfoDialogFragmentBinding getViewBinding() {
        return LightControlInfoDialogFragmentBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<LightControlViewModel> getViewModelClass() {
        return LightControlViewModel.class;
    }

    @Override
    protected void bindVars() {
        binding.setViewModel(viewModel);
        binding.setOkClickListener(this);
    }

    @Override
    protected LifecycleOwner observeData() {
        return NavHostFragment.findNavController(this).getCurrentBackStackEntry();
    }

    @Override
    protected ViewModelProvider getViewModelProvider() {
        // NavBackStackEntry and viewModel scoped to our nested nav graph (containing all light
        // detail screens).
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_nested_light_control);
        return new ViewModelProvider(backStackEntry);
    }

    @Override
    public void onOkClick() {
        dismiss();
    }
}
