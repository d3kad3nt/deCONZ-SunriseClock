package org.d3kad3nt.sunriseClock.deviceControl;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import org.d3kad3nt.sunriseClock.R;

public class ControlActivity extends AppCompatActivity {

    private static final String TAG = "ControlActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = new Bundle();
        args.putLong("Light", getIntent().getLongExtra("Light", -1));
        args.putString("LightName", getIntent().getStringExtra("LightName"));
        Log.d(TAG, args.toString());
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_device_control);
        setContentView(R.layout.activity_device_control);
//        setContentView(binding.getRoot());
        NavHostFragment navHostFragment =
            (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.device_control_activity_nav_fragment);
        if (navHostFragment == null) {
            throw new IllegalStateException("Could not get NavHostFragment");
        }
        NavController navController = navHostFragment.getNavController();

        navController.setGraph(R.navigation.nav_graph_device_control, args);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

//        navController.navigate(R.id.lightDetail, args);
//        setSupportActionBar(binding.toolbar);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(binding.navigationView, navController);
    }
}
