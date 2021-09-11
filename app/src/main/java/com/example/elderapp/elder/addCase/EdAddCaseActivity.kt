package com.example.elderapp.elder.addCase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.elderapp.Global
import com.example.elderapp.R
import com.example.elderapp.adapter.User
import com.google.gson.Gson
import org.json.JSONException
import java.util.HashMap

class EdAddCaseActivity : AppCompatActivity() {
    lateinit var navController: NavController

    private val viewModel: AddCaseViewModel by viewModels()
     var uid: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ed_add_case)
        val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.fragment) as NavHostFragment

        navController = navHostFragment.navController
        findViewById<LinearLayout>(R.id.btn_back).setOnClickListener {
            finish()
        }

        uid = getSharedPreferences("loginUser", AppCompatActivity.MODE_PRIVATE).getString("uid", "0")?.toInt()
                ?: -1

    }

    fun createCase() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, Global.url + "case/create.php", Response.Listener { response: String? ->
            Log.d("request", "select Response: $response")
            try {
                Toast.makeText(this,if(response=="ok") "新增成功" else "錯誤",Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error: VolleyError? -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["uid"] = uid.toString()
                data["date"] = viewModel.date.value
                data["place"] = viewModel.place.value.toString()
                data["invite"] = viewModel.invite.value
                data["sex_limit"] = viewModel.limit.value
                if(viewModel.public.value!=null) data["public"] = viewModel.public.value
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}