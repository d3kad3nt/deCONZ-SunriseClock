package org.d3kad3nt.sunriseClock.ui.light.lightDetail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.databinding.LightDetailFragmentBinding;

public class LightDetailFragment extends Fragment implements MenuProvider {

    private static final String TAG = "LightDetailFragment";

    private LightDetailFragmentBinding binding;
    private LightDetailViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        long lightID = LightDetailFragmentArgs.fromBundle(requireArguments()).getLight(); // id from navigation
        // Use custom factory to initialize viewModel with light id (instead of using new ViewModelProvider(this)
        // .get(LightDetailViewModel.class))
        viewModel = new ViewModelProvider(this,
            new LightDetailViewModelFactory(requireActivity().getApplication(), lightID)).get(
            LightDetailViewModel.class);

        binding = LightDetailFragmentBinding.inflate(inflater, container, false);

        // Initialize the options menu (toolbar menu).
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(this, getViewLifecycleOwner());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.setViewModel(viewModel);
        // Specify the fragment view as the lifecycle owner of the binding. This is used so that the binding can
        // observe LiveData updates.
        binding.setLifecycleOwner(getViewLifecycleOwner());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateMenu(@NonNull final Menu menu, @NonNull final MenuInflater menuInflater) {
        // XML menu resources do not support view or data binding: We have to use the R class.
        Log.d(TAG, "Adding menu options to the toolbar.");
        menuInflater.inflate(R.menu.menu_light_details, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull final MenuItem menuItem) {
        // The SwipeRefreshLayout does not provide accessibility events.
        // Instead, a menu item should be provided to allow refresh of the content wherever this gesture is used.
        if (menuItem.getItemId() == R.id.menu_light_details_refresh) {
            Log.d(TAG, "User requested a light refresh by clicking the toolbar menu option.");
            viewModel.refreshLight();
            return true;
        }
        return false;
    }
}
