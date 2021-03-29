package org.d3kad3nt.sunriseClock.ui.mainWindow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.d3kad3nt.sunriseClock.databinding.EndpointAddDeconzFragmentBinding;
import org.d3kad3nt.sunriseClock.databinding.EndpointAddFragmentBinding;
import org.d3kad3nt.sunriseClock.ui.viewModel.EndpointAddViewModel;

import java.util.HashMap;
import java.util.Map;

public class EndpointAddFragment extends Fragment {

    private static final String TAG = "EndpointAddFragment";

    private EndpointAddViewModel viewModel;

    public static EndpointInfoFragment newInstance() {
        return new EndpointInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        EndpointAddFragmentBinding binding = EndpointAddFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(EndpointAddViewModel.class);
        //TODO select endpoint type
        EndpointAddDeconzFragmentBinding deconzBinding = EndpointAddDeconzFragmentBinding.inflate(inflater,binding.constraintLayoutSpecificEndpoint,true);
        addCreateEndpointListener(binding,deconzBinding);
        return binding.getRoot();
    }

    private void addCreateEndpointListener(EndpointAddFragmentBinding binding, EndpointAddDeconzFragmentBinding specificBinding) {
        binding.createEndpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> settings = new HashMap<>();
                settings.put("name",binding.editTextEndpointName.getText().toString());
                settings.put("type",specificBinding.getRoot().getTag().toString());
                ViewGroup rootLinearLayout = specificBinding.getRoot();
                int count = rootLinearLayout.getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = rootLinearLayout.getChildAt(i);
                    if (child instanceof EditText) {
                        EditText input =(EditText)child;
                        String text = input.getText().toString();
                        Object tag = input.getTag();
                        if (tag != null){
                            settings.put(tag.toString(), text);
                        }else{
                            Log.e(TAG, "Unknown field in " + specificBinding.getClass().getSimpleName());
                        }
                    }
                }
            }
        });
    }

}
