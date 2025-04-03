package org.d3kad3nt.sunriseClock.ui.endpoint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.google.android.material.textfield.TextInputEditText;
import java.util.HashMap;
import java.util.Map;
import org.d3kad3nt.sunriseClock.databinding.EndpointAddDeconzFragmentBinding;
import org.d3kad3nt.sunriseClock.databinding.EndpointAddFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.util.BaseFragment;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class EndpointAddFragment extends BaseFragment<EndpointAddFragmentBinding, EndpointAddViewModel> {

    @Override
    protected EndpointAddFragmentBinding getViewBinding(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState) {
        EndpointAddFragmentBinding tmpBinding = EndpointAddFragmentBinding.inflate(inflater, container, false);

        // TODO: select endpoint type. This whole binding section should be refactored. Maybe use separate screens
        //  for every endpoint type instead of this dynamic binding.
        EndpointAddDeconzFragmentBinding deconzBinding =
                EndpointAddDeconzFragmentBinding.inflate(inflater, tmpBinding.constraintLayoutSpecificEndpoint, true);

        return addCreateEndpointListener(tmpBinding, deconzBinding);
    }

    @Override
    protected Class<EndpointAddViewModel> getViewModelClass() {
        return EndpointAddViewModel.class;
    }

    @Override
    protected void bindVars(final EndpointAddFragmentBinding binding) {}

    @Override
    protected LifecycleOwner getLifecycleOwner() {
        return getViewLifecycleOwner();
    }

    @Override
    protected ViewModelProvider getViewModelProvider() {
        return new ViewModelProvider(this);
    }

    // Todo: This should definitely be removed (and replaced by setting the onClickListener inside of XML and
    //  carrying over the logic to the viewmodel).
    @NonNull
    private EndpointAddFragmentBinding addCreateEndpointListener(
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
        return binding;
    }
}
