package org.d3kad3nt.sunriseClock.ui.endpoint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import java.util.List;
import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.data.model.endpoint.IEndpointUI;
import org.d3kad3nt.sunriseClock.databinding.EndpointsFragmentBinding;
import org.d3kad3nt.sunriseclock.util.LogUtil;

public class EndpointsFragment extends Fragment implements EndpointsListAdapter.ClickListeners {

    private EndpointsFragmentBinding binding;
    private EndpointsViewModel viewModel;
    private EndpointsListAdapter adapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.d("Show endpoint list view");
        viewModel = new ViewModelProvider(requireActivity()).get(EndpointsViewModel.class);

        binding = EndpointsFragmentBinding.inflate(inflater, container, false);

        adapter = new EndpointsListAdapter(viewModel, this);
        binding.recyclerView.setAdapter(adapter);

        viewModel.getEndpoints().observe(getViewLifecycleOwner(), new Observer<List<IEndpointUI>>() {
            @Override
            public void onChanged(List<IEndpointUI> endpointConfigList) {
                if (!endpointConfigList.isEmpty()) {
                    adapter.submitList(endpointConfigList);
                } else {
                    LogUtil.d("No Endpoints found");
                }
            }
        });
        addAddEndpointListener(binding);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(view);

        // In some cases, you might need to define multiple top-level destinations instead of using
        // the default start
        // destination.
        // Using a BottomNavigationView is a common use case for this, where you may have sibling
        // screens that are
        // not hierarchically related to each other and may each have their own set of related
        // destinations.
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.lightsList, R.id.endpointsList, R.id.mainSettingsFragment).build();

        NavigationUI.setupWithNavController(binding.endpointsToolbar, navController, appBarConfiguration);

        // Specify the fragment view as the lifecycle owner of the binding. This is used so that the
        // binding can
        // observe LiveData updates.
        binding.setLifecycleOwner(getViewLifecycleOwner());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void addAddEndpointListener(@NonNull EndpointsFragmentBinding binding) {
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("Navigate to endpoint creation view");
                Navigation.findNavController(v)
                        .navigate(EndpointsFragmentDirections.actionEndpointsToEndpointAddFragment());
            }
        });
    }

    @Override
    public void onCardClick(final View view, final long endpointId, String endpointName) {
        LogUtil.d("Navigate to endpoint detail view for endpoint %s (Id %d)", endpointName, endpointId);
        Navigation.findNavController(view)
                .navigate(EndpointsFragmentDirections.actionEndpointsToEndpointDetail(endpointId, endpointName));
    }
}
