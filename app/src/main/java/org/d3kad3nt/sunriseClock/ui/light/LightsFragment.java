package org.d3kad3nt.sunriseClock.ui.light;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.data.model.light.UILight;
import org.d3kad3nt.sunriseClock.data.model.resource.Resource;
import org.d3kad3nt.sunriseClock.data.model.resource.Status;
import org.d3kad3nt.sunriseClock.databinding.LightsFragmentBinding;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LightsFragment extends Fragment implements LightsListAdapter.ClickListeners, MenuProvider {

    private static final String TAG = "LightsFragment";
    private final LightsState lightsState = new LightsState();
    private LightsFragmentBinding binding;
    private LightsViewModel viewModel;
    private LightsListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(LightsViewModel.class);

        binding = LightsFragmentBinding.inflate(inflater, container, false);

        binding.setLightsState(lightsState);

        adapter = new LightsListAdapter(this);
        binding.recyclerView.setAdapter(adapter);

        // Initialize the options menu (toolbar menu).
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(this, getViewLifecycleOwner());

        viewModel.getLights().observe(getViewLifecycleOwner(), new Observer<Resource<List<UILight>>>() {
            @Override
            public void onChanged(Resource<List<UILight>> listResource) {
                Log.d(TAG, listResource.getStatus().toString());
                if (listResource.getStatus().equals(Status.SUCCESS) && listResource.getData() != null) {
                    lightsState.clearError();
                    List<UILight> list = listResource.getData();
                    Collections.sort(list, new Comparator<>() {
                        @Override
                        public int compare(final UILight uiLight, final UILight uiLight2) {
                            return uiLight.getName().compareTo(uiLight2.getName());
                        }
                    });
                    adapter.submitList(list);
                } else if (listResource.getStatus().equals(Status.ERROR)) {
                    lightsState.setError(getResources().getString(R.string.noLights_title),
                        listResource.getMessage());
                    adapter.submitList(List.of());
                }
            }
        });

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
    public void onCardClick(View view, final long lightId, final String lightName) {
        Navigation.findNavController(view).navigate(LightsFragmentDirections.actionLightsToLightDetail(lightId, lightName));
    }

    @Override
    public void onSwitchCheckedChange(final long lightId, final boolean state) {
        viewModel.setLightOnState(lightId, state);
    }

    @Override
    public void onSliderTouch(final long lightId, @IntRange(from = 0, to = 100) final int brightness, final boolean state) {
        viewModel.setLightBrightness(lightId, brightness, state);
    }

    @Override
    public void onCreateMenu(@NonNull final Menu menu, @NonNull final MenuInflater menuInflater) {
        // XML menu resources do not support view or data binding: We have to use the R class.
        Log.d(TAG, "Adding menu options to the toolbar.");
        menuInflater.inflate(R.menu.menu_lights, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull final MenuItem menuItem) {
        // The SwipeRefreshLayout does not provide accessibility events.
        // Instead, a menu item should be provided to allow refresh of the content wherever this gesture is used.
        if (menuItem.getItemId() == R.id.menu_lights_refresh) {
            Log.d(TAG, "User requested refresh of all lights by clicking the toolbar menu option.");
            viewModel.refreshLights();
            return true;
        }
        return false;
    }
}
