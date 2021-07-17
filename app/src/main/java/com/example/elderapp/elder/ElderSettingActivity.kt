package com.example.elderapp.elder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import com.example.elderapp.R

class ElderSettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elder_setting)

        val btnBack = findViewById<Button>(R.id.btn_back)
        val btnPlace = findViewById<LinearLayout>(R.id.btn_place)
        val btnProfile = findViewById<LinearLayout>(R.id.btn_profile)
        val btnFriend = findViewById<LinearLayout>(R.id.btn_friend)
        val btnLogout = findViewById<LinearLayout>(R.id.btn_logout)

        btnBack.setOnClickListener { finish() }
        btnPlace.setOnClickListener { startActivity(Intent(this, EditPlaceActivity::class.java)) }

    }
}