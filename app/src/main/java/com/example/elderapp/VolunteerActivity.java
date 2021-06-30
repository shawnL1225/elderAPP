package com.example.elderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class VolunteerActivity extends AppCompatActivity {

    AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        BottomNavigationView mBottomNav = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(mBottomNav, navController);

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.caseFragment, R.id.vtEventFragment, R.id.hourRecordFragment).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


    }


}