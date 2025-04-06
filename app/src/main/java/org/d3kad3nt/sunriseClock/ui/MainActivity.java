package org.d3kad3nt.sunriseClock.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import org.d3kad3nt.sunriseClock.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        org.d3kad3nt.sunriseClock.databinding.ActivityMainBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_main);
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(binding.mainNavHostFragment.getId());

        assert navHostFragment != null;

        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.mainBottomNavigation, navController);
    }
}
