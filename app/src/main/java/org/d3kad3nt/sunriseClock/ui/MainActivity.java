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
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment =
            (NavHostFragment) getSupportFragmentManager().findFragmentById(binding.mainNavHostFragment.getId());

        assert navHostFragment != null;

        navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.mainBottomNavigation, navController);
    }
}
