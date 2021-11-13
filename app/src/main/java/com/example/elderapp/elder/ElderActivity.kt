package com.example.elderapp.elder

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.elderapp.Global
import com.example.elderapp.R
import com.example.elderapp.RawUser

class ElderActivity : AppCompatActivity() {
    private var profileImage :ImageView? = null
    private var txtName :TextView? = null
    private var txtPhone :TextView? = null
    public var btnBack :Button? = null
    private var navController: NavController? = null
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
        btnBack = findViewById(R.id.btn_back)
        navController = Navigation.findNavController(this, R.id.fragment_elder)
        uid = getSharedPreferences("loginUser", MODE_PRIVATE).getString("uid", "")
        btnBack!!.setOnClickListener {
            navController!!.popBackStack()
            btnBack!!.visibility = View.GONE
        }

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