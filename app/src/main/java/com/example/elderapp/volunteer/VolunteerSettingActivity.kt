package com.example.elderapp.volunteer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import com.example.elderapp.LoginActivity
import com.example.elderapp.R

class VolunteerSettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_setting)

        val btnBack = findViewById<Button>(R.id.btn_back)

        val btnProfile = findViewById<LinearLayout>(R.id.btn_profile)
        val btnFriend = findViewById<LinearLayout>(R.id.btn_friend)
        val btnLogout = findViewById<LinearLayout>(R.id.btn_logout)
        val btnDiscuss = findViewById<LinearLayout>(R.id.btn_disscus)

        btnDiscuss.setOnClickListener { startActivity(Intent(this, DiscussTitleActivity::class.java)) }
        btnBack.setOnClickListener { finish() }
        btnFriend.setOnClickListener { startActivity(Intent(this, VtFriendActivity::class.java)) }
        btnProfile.setOnClickListener { startActivity(Intent(this, VtProfileActivity::class.java)) }
        btnLogout.setOnClickListener {
            getSharedPreferences("loginUser", MODE_PRIVATE)
                    .edit().putString("uid", "").apply()
            startActivity(Intent(this, LoginActivity::class.java))

        }
    }
}