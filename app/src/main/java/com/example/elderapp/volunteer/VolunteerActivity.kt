package com.example.elderapp.volunteer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.elderapp.Global
import com.example.elderapp.R
import com.example.elderapp.RawUser
import com.google.android.material.bottomnavigation.BottomNavigationView

class VolunteerActivity : AppCompatActivity() {
    private var profileImage :ImageView? = null
    private var txtName :TextView? = null
    private var txtPhone :TextView? = null
    var uid :String? = null
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

        profileImage = findViewById<ImageView>(R.id.profile_image)
        txtName = findViewById<TextView>(R.id.txt_name)
        txtPhone = findViewById<TextView>(R.id.txt_phone)

        uid = getSharedPreferences("loginUser", MODE_PRIVATE).getString("uid", "")


    }
    override fun onResume() {
        super.onResume()
        Log.d("life", "onResume")
        Global.profile(this, uid!!) { user: RawUser ->
            Global.headUp(this,profileImage!!,user.headshot)
            txtName!!.text = user.name+" (志工)"
            txtPhone!!.text = user.phone
            getSharedPreferences("loginUser", MODE_PRIVATE).edit()
                    .putString("name",  user.name).apply()
        }
    }

}