package org.d3kad3nt.sunriseClock.ui.endpointManagement;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.databinding.EndpointManagementNameEditDialogFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.util.BaseDialogFragment;
import org.d3kad3nt.sunriseClock.ui.util.DialogCancelClickListener;
import org.d3kad3nt.sunriseClock.ui.util.DialogOkClickListener;

public class EndpointManagementNameEditDialogFragment
        extends BaseDialogFragment<EndpointManagementNameEditDialogFragmentBinding, EndpointManagementViewModel>
        implements DialogCancelClickListener, DialogOkClickListener {

    @Override
    protected EndpointManagementNameEditDialogFragmentBinding getViewBinding() {
        return EndpointManagementNameEditDialogFragmentBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<EndpointManagementViewModel> getViewModelClass() {
        return EndpointManagementViewModel.class;
    }

    @Override
    protected void bindVars() {
        binding.setViewModel(viewModel);
        binding.setCancelClickListener(this);
        binding.setOkClickListener(this);
    }

    @Override
    protected LifecycleOwner observeData() {
        return NavHostFragment.findNavController(this).getCurrentBackStackEntry();
    }

    @Override
    protected ViewModelProvider getViewModelProvider() {
        // NavBackStackEntry and viewModel scoped to our nested nav graph (containing all endpoint
        // detail screens).
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_nested_endpoint_management);
        return new ViewModelProvider(backStackEntry);
    }

    @Override
    public void onCancelClick() {
        dismiss();
    }

    @Override
    public void onOkClick() {
        // Todo: Update toolbar title to reflect endpoint name change.
        viewModel.setEndpointNameFromEditText();
        dismiss();
    }
}
