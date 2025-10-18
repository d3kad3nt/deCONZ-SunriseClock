package org.d3kad3nt.sunriseClock.ui.endpoint.endpointDetail;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.databinding.EndpointDetailDeleteDialogFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.util.BaseDialogFragment;
import org.d3kad3nt.sunriseClock.ui.util.DialogCancelClickListener;
import org.d3kad3nt.sunriseClock.ui.util.DialogOkClickListener;

public class EndpointDetailDeleteDialogFragment
        extends BaseDialogFragment<EndpointDetailDeleteDialogFragmentBinding, EndpointDetailViewModel>
        implements DialogCancelClickListener, DialogOkClickListener {

    @Override
    protected EndpointDetailDeleteDialogFragmentBinding getViewBinding() {
        return EndpointDetailDeleteDialogFragmentBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<EndpointDetailViewModel> getViewModelClass() {
        return EndpointDetailViewModel.class;
    }

    @Override
    protected void bindVars() {
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
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_graph_endpoint_detail);
        return new ViewModelProvider(backStackEntry);
    }

    @Override
    public void onCancelClick() {
        dismiss();
    }

    @Override
    public void onOkClick() {
        viewModel.deleteEndpoint();
        dismiss();
        // After deleting the endpoint, we should navigate back to the list of endpoints.
        NavHostFragment.findNavController(this).popBackStack(R.id.bottomnav_endpoints, false);
    }
}
