package org.d3kad3nt.sunriseClock.ui.light.lightDetail;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.databinding.LightDetailInfoDialogFragmentBinding;
import org.d3kad3nt.sunriseClock.util.LogUtil;

import java.util.Objects;

public class LightDetailInfoDialogFragment extends DialogFragment {

    private LightDetailInfoDialogFragmentBinding binding;

    private LightDetailViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        LogUtil.d("Show light detail info view");

        // NavBackStackEntry and viewModel scoped to our nested nav graph (containing all light detail screens).
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_graph_light_detail);
        viewModel = new ViewModelProvider(backStackEntry).get(LightDetailViewModel.class);

        binding = LightDetailInfoDialogFragmentBinding.inflate(inflater, container, false);

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow())
            .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        binding.setViewModel(viewModel);
        //  When subscribing to lifecycle-aware components such as LiveData,
        //  never use viewLifecycleOwner as the LifecycleOwner in a DialogFragment that uses Dialog objects.
        //  Instead, use the DialogFragment itself, or, if you're using Jetpack Navigation, use the NavBackStackEntry.
        binding.setLifecycleOwner(NavHostFragment.findNavController(this).getCurrentBackStackEntry());
    }
}
