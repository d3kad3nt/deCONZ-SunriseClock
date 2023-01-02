package org.d3kad3nt.sunriseClock.ui.endpoint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;

import org.d3kad3nt.sunriseClock.databinding.EndpointAddDeconzFragmentBinding;
import org.d3kad3nt.sunriseClock.databinding.EndpointAddFragmentBinding;

import java.util.HashMap;
import java.util.Map;

public class EndpointAddFragment extends Fragment {

    private static final String TAG = "EndpointAddFragment";

    private EndpointAddViewModel viewModel;

    public static EndpointAddFragment newInstance() {
        return new EndpointAddFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        EndpointAddFragmentBinding binding = EndpointAddFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(EndpointAddViewModel.class);
        //TODO select endpoint type
        EndpointAddDeconzFragmentBinding deconzBinding = EndpointAddDeconzFragmentBinding.inflate(inflater,
                binding.constraintLayoutSpecificEndpoint, true);
        addCreateEndpointListener(binding, deconzBinding);
        return binding.getRoot();
    }

    //Todo: This should definitely be removed (and replaced by setting the onClickListener inside of XML and
    // carrying over the logic to the viewmodel)
    private void addCreateEndpointListener(EndpointAddFragmentBinding binding,
            EndpointAddDeconzFragmentBinding specificBinding) {
        binding.createEndpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> settings = new HashMap<>();
                settings.put("name", binding.endpointName.getText()
                                                         .toString());
                settings.put("type", specificBinding.getRoot()
                                                    .getTag()
                                                    .toString());
                ViewGroup rootLinearLayout = (ViewGroup) specificBinding.getRoot();
                TextInputEditText[] input = {rootLinearLayout.findViewWithTag(
                        "baseUrl"), rootLinearLayout.findViewWithTag("port"), rootLinearLayout.findViewWithTag(
                        "apiKey")};
                for (TextInputEditText i : input) {
                    settings.put(i.getTag()
                                  .toString(), i.getText()
                                                .toString());
                }
                if (viewModel.createEndpoint(settings)) {
                    Navigation.findNavController(v)
                              .navigateUp();
                }
            }
        });
    }

}
