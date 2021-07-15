package com.example.elderapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.elderapp.LoginActivity
import com.example.elderapp.elder.ElderActivity
import com.example.elderapp.register.RegisterActivity
import com.example.elderapp.volunteer.VolunteerActivity
import java.util.*

class LoginActivity : AppCompatActivity() {
    var account: String? = null
    var password: String? = null
    var etAccount: EditText? = null
    var etPassword: EditText? = null
    var url: String? = Global.url + "login.php"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etAccount = findViewById(R.id.et_phone)
        etPassword = findViewById(R.id.et_password)
        val isSignUp = intent.getBooleanExtra("signUp", false)
        if (isSignUp) Global.putSnackBar(etAccount, "已註冊成功 !")
    }

    fun toRegister(view: View?) {
        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
    }

    fun login(view: View?) {
        account = etAccount.getText().toString().trim { it <= ' ' }
        password = etPassword.getText().toString().trim { it <= ' ' }
        if (account == "") {
            Global.putSnackBarR(etAccount, "請輸入帳號")
        } else {
            SQL()
        }
    }

    private fun SQL() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String? ->
            Log.d("connect", "Response: $response")
            if (response.startsWith("success")) {
                // formation would be "success-userID-userIdentity"
                val sep: Array<String?> = response.split("-".toRegex()).toTypedArray()
                getSharedPreferences("mySP", MODE_PRIVATE)
                        .edit().putString("uid", sep[1]).apply()
                var it = Intent(this@LoginActivity, ElderActivity::class.java)
                if (sep[2] == "1") {
                    it = Intent(this@LoginActivity, VolunteerActivity::class.java)
                }
                startActivity(it)
                //                    finish();
            } else if (response == "NoUser") {
                Toast.makeText(this@LoginActivity, "登入失敗 - 查無此使用者", Toast.LENGTH_SHORT).show()
            } else if (response == "WrongPass") {
                Toast.makeText(this@LoginActivity, "登入失敗 - 密碼錯誤", Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error: VolleyError? -> Toast.makeText(this@LoginActivity, error.toString().trim { it <= ' ' }, Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?>? {
                val data: MutableMap<String?, String?> = HashMap()
                data["phone"] = account
                data["password"] = password
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}