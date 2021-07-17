package com.example.elderapp.elder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.elderapp.R

class ElderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elder)

        val btnSetting = findViewById<LinearLayout>(R.id.btn_setting)


        btnSetting.setOnClickListener {
           startActivity(Intent(this, ElderSettingActivity::class.java))
        }


    }
}