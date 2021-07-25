package com.example.elderapp.elder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.elderapp.Global
import com.example.elderapp.R
import com.example.elderapp.RawUser

class ElderActivity : AppCompatActivity() {
    private var profileImage :ImageView? = null
    private var txtName :TextView? = null
    private var txtPhone :TextView? = null
    var uid :String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elder)

        val btnSetting = findViewById<LinearLayout>(R.id.btn_setting)

        btnSetting.setOnClickListener {
           startActivity(Intent(this, ElderSettingActivity::class.java))
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
            txtName!!.text = user.name
            txtPhone!!.text = user.phone
            getSharedPreferences("loginUser", MODE_PRIVATE).edit()
                    .putString("name",  user.name).apply()
        }
    }
}