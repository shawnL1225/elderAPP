package com.example.elderapp.volunteer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.elderapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class VolunteerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer)
        val mBottomNav = findViewById<BottomNavigationView?>(R.id.bottomNavigationView)
        val navController = Navigation.findNavController(this, R.id.fragment_volunteer)
        NavigationUI.setupWithNavController(mBottomNav, navController)

//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.caseFragment, R.id.vtEventFragment, R.id.hourRecordFragment).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }
}