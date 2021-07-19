package com.example.elderapp.volunteer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.elderapp.Global
import com.example.elderapp.R
import com.example.elderapp.RawUser
import com.google.android.material.bottomnavigation.BottomNavigationView

class VolunteerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer)

        val mBottomNav = findViewById<BottomNavigationView?>(R.id.bottomNavigationView)
        val navController = Navigation.findNavController(this, R.id.fragment_volunteer)
        NavigationUI.setupWithNavController(mBottomNav, navController)

        val btnSetting = findViewById<LinearLayout>(R.id.btn_setting)
        btnSetting.setOnClickListener {
            startActivity(Intent(this, VolunteerSettingActivity::class.java))
        }

        Global.profile(this, "72") { user: RawUser ->
            Log.d("Name", user.name)
        }
    }

}