package org.d3kad3nt.sunriseClock.ui.endpoint.endpointDetail;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.databinding.EndpointDetailInfoDialogFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.util.BaseDialogFragment;
import org.d3kad3nt.sunriseClock.ui.util.DialogOkClickListener;

public class EndpointDetailInfoDialogFragment
    extends BaseDialogFragment<EndpointDetailInfoDialogFragmentBinding, EndpointDetailViewModel>
    implements DialogOkClickListener {

    @Override
    protected EndpointDetailInfoDialogFragmentBinding getViewBinding() {
        return EndpointDetailInfoDialogFragmentBinding.inflate(getLayoutInflater());
    }

    @Override
    protected Class<EndpointDetailViewModel> getViewModelClass() {
        return EndpointDetailViewModel.class;
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
        // NavBackStackEntry and viewModel scoped to our nested nav graph (containing all endpoint detail screens).
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_graph_endpoint_detail);
        return new ViewModelProvider(backStackEntry);
    }

    @Override
    public void onOkClick() {
        dismiss();
    }
}
