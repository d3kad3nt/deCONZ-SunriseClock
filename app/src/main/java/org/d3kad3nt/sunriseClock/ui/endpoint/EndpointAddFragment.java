package org.d3kad3nt.sunriseClock.ui.endpoint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.textfield.TextInputEditText;
import java.util.HashMap;
import java.util.Map;
import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.databinding.EndpointAddDeconzFragmentBinding;
import org.d3kad3nt.sunriseClock.databinding.EndpointAddFragmentBinding;
import org.d3kad3nt.sunriseclock.util.LogUtil;

public class EndpointAddFragment extends Fragment {

    private EndpointAddFragmentBinding binding;
    private EndpointAddViewModel viewModel;

    public static EndpointAddFragment newInstance() {
        return new EndpointAddFragment();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.d("Show add endpoint view");

        viewModel = new ViewModelProvider(requireActivity()).get(EndpointAddViewModel.class);

        binding = EndpointAddFragmentBinding.inflate(inflater, container, false);

        // TODO select endpoint type
        EndpointAddDeconzFragmentBinding deconzBinding =
                EndpointAddDeconzFragmentBinding.inflate(inflater, binding.constraintLayoutSpecificEndpoint, true);
        addCreateEndpointListener(binding, deconzBinding);

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

        NavigationUI.setupWithNavController(binding.endpointAddToolbar, navController, appBarConfiguration);

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

    // Todo: This should definitely be removed (and replaced by setting the onClickListener inside
    // of XML and
    // carrying over the logic to the viewmodel)
    private void addCreateEndpointListener(
            @NonNull EndpointAddFragmentBinding binding, EndpointAddDeconzFragmentBinding specificBinding) {
        binding.createEndpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> settings = new HashMap<>();
                settings.put("name", binding.endpointName.getText().toString());
                settings.put("type", specificBinding.getRoot().getTag().toString());
                ViewGroup rootLinearLayout = (ViewGroup) specificBinding.getRoot();
                TextInputEditText[] input = {
                    rootLinearLayout.findViewWithTag("baseUrl"),
                    rootLinearLayout.findViewWithTag("port"),
                    rootLinearLayout.findViewWithTag("apiKey")
                };
                for (TextInputEditText i : input) {
                    settings.put(i.getTag().toString(), i.getText().toString());
                }
                if (viewModel.createEndpoint(settings)) {
                    LogUtil.v("Endpoint created");
                    Navigation.findNavController(v).navigateUp();
                }
            }
        });
    }
}
