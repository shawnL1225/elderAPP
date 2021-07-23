package com.example.elderapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AddEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        val btnBack = findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener { finish() }
    }
}