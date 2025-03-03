package org.d3kad3nt.sunriseClock.ui.light.lightDetail;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.databinding.LightDetailInfoDialogFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.util.BaseDialogFragment;
import org.d3kad3nt.sunriseClock.ui.util.DialogOkClickListener;

public class LightDetailInfoDialogFragment extends BaseDialogFragment<LightDetailInfoDialogFragmentBinding, LightDetailViewModel> implements DialogOkClickListener {

    @Override
    protected LightDetailInfoDialogFragmentBinding getViewBinding() {
        return LightDetailInfoDialogFragmentBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<LightDetailViewModel> getViewModelClass() {
        return LightDetailViewModel.class;
    }

    @Override
    protected ViewModelProvider getViewModelProvider() {
        // NavBackStackEntry and viewModel scoped to our nested nav graph (containing all light detail screens).
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_graph_light_detail);
        return new ViewModelProvider(backStackEntry);
    }

    @Override
    protected void bindVars() {
        binding.setViewModel(viewModel);
        binding.setOkClickListener(this);
    }

    @Override
    protected void observeData() {
        //  When subscribing to lifecycle-aware components such as LiveData,
        //  never use viewLifecycleOwner as the LifecycleOwner in a DialogFragment that uses Dialog objects.
        //  Instead, use the DialogFragment itself, or, if you're using Jetpack Navigation, use the NavBackStackEntry.
        binding.setLifecycleOwner(NavHostFragment.findNavController(this).getCurrentBackStackEntry());
    }

    @Override
    public void onOkClick() {
        dismiss();
    }
}
