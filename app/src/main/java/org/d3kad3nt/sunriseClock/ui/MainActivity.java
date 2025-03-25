package org.d3kad3nt.sunriseClock.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment)
            getSupportFragmentManager().findFragmentById(binding.mainNavHostFragment.getId());

        assert navHostFragment != null;

        navController = navHostFragment.getNavController();
        // In some cases, you might need to define multiple top-level destinations instead of using
        // the default start destination.
        // Using a BottomNavigationView is a common use case for this, where you may have sibling
        // screens that are not hierarchically related to each other and may each have their own set
        // of related destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
            R.id.lightsList, R.id.endpointsList, R.id.mainSettingsFragment)
            .build();

        setSupportActionBar(binding.mainToolbar);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.mainBottomNavigation, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
            || super.onSupportNavigateUp();
    }
}
