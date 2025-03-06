package org.d3kad3nt.sunriseClock.deviceControl;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.databinding.ActivityDeviceControlBinding;

/**
 * This Activity is used for the Device Control Service. A own service for this is needed, because it uses a own
 * navgraph and a own fragment for the activity without a drawer.
 * <p>
 * It is possible that this can also be archived purely by using Navigation functions, but i didn't find a way to do
 * that.
 */
public class ControlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityDeviceControlBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_device_control);
        setContentView(binding.getRoot());
        NavHostFragment navHostFragment =
            (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.device_control_activity_nav_fragment);
        if (navHostFragment == null) {
            throw new IllegalStateException("Could not get NavHostFragment");
        }
        NavController navController = navHostFragment.getNavController();

        navController.setGraph(R.navigation.nav_graph_device_control, getIntent().getExtras());
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

        setSupportActionBar(binding.toolbar);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }
}
