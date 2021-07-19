package com.example.elderapp.elder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.elderapp.Global
import com.example.elderapp.R
import com.example.elderapp.RawUser

class ElderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elder)

        val btnSetting = findViewById<LinearLayout>(R.id.btn_setting)

        btnSetting.setOnClickListener {
           startActivity(Intent(this, ElderSettingActivity::class.java))
        }

        val profileImage = findViewById<ImageView>(R.id.profile_image)
        val txtName = findViewById<TextView>(R.id.txt_name)
        val txtPhone = findViewById<TextView>(R.id.txt_phone)

        val uid = getSharedPreferences("loginUser", MODE_PRIVATE).getString("uid", "")

        Global.profile(this, uid!!) { user: RawUser ->
            Global.headup(this,profileImage,user.headshot)
            txtName.text = user.name
            txtPhone.text = user.phone
        }



    }
}