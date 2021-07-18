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
        if (isSignUp) Global.putSnackBar(etPassword!!, "已註冊成功 !")

        val uid = getSharedPreferences("loginUser", MODE_PRIVATE).getString("uid", "")
        val uIdentity = getSharedPreferences("loginUser", MODE_PRIVATE).getString("uIdentity", "")
        if(uid != ""){
            if(uIdentity == "0") startActivity(Intent(this, ElderActivity::class.java))
            if(uIdentity == "1") startActivity(Intent(this, VolunteerActivity::class.java))
        }
    }

    fun toRegister(view: View) {
        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
    }

    fun login(view: View) {
        account = etAccount?.text.toString().trim { it <= ' ' }
        password = etPassword?.text.toString().trim()
        if (account == "") {
            Global.putSnackBarR(etAccount!!, "請輸入帳號")
        } else {
            requestCheckUser()
        }
    }

    private fun requestCheckUser() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String ->
            Log.d("connect", "Response: $response")
            when {
                response.startsWith("success") -> {
                    // formation would be "success-userID-userIdentity"
                    val sep: Array<String?> = response.split("-").toTypedArray()
                    getSharedPreferences("loginUser", MODE_PRIVATE).edit()
                            .putString("uid", sep[1])
                            .putString("uIdentity", sep[2])
                            .apply()

                    var it = Intent(this@LoginActivity, ElderActivity::class.java)
                    if (sep[2] == "1") {
                        it = Intent(this@LoginActivity, VolunteerActivity::class.java)
                    }
                    startActivity(it)
                    //                    finish();
                }
                response == "NoUser" -> {
                    Toast.makeText(this@LoginActivity, "登入失敗 - 查無此使用者", Toast.LENGTH_SHORT).show()
                }
                response == "WrongPass" -> {
                    Toast.makeText(this@LoginActivity, "登入失敗 - 密碼錯誤", Toast.LENGTH_SHORT).show()
                }
            }
        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(this@LoginActivity, error.toString() , Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
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