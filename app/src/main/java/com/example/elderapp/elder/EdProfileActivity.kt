package com.example.elderapp.elder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.elderapp.R
import com.example.elderapp.volunteer.VolunteerActivity
import java.util.HashMap

class EdProfileActivity : AppCompatActivity() {
    private var etName: EditText? = null
    private var etPhone: EditText? = null
    private var etPassword: EditText? = null
    private var etPasswordC: EditText? = null
    private var etRemarksIll: EditText? = null
    private var etRemarksEating: EditText? = null
    private var etRemarksOther: EditText? = null
    private var etContactPhone: EditText? = null
    private var etContactEmail: EditText? = null
    private var etAddress: EditText? = null
    private var username: String? = null
    private var phone: String? = null
    private var pass: String? = null
    private var passC: String? = null
    private var remarksIll: String? = ""
    private var remarksEating: String? = ""
    private var remarksOther: String? = ""
    private var contactPhone: String? = null
    private var contactEmail: String? = null
    private var sex: String? = null
    private var addr: String? = null
    private val url: String? = "https://www2.cs.ccu.edu.tw/~lwx109u/elderApp/register.php"
    var uid :String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ed_profile)

        uid = getSharedPreferences("loginUser", MODE_PRIVATE).getString("uid", "")
        etName = findViewById(R.id.et_name)
        etPhone = findViewById(R.id.et_phone)
        etPassword = findViewById(R.id.et_password)
        etPasswordC = findViewById(R.id.et_passwordCheck)
        etRemarksIll = findViewById(R.id.et_remarks1)
        etRemarksEating = findViewById(R.id.et_remarks2)
        etRemarksOther = findViewById(R.id.et_remarks3)
        etContactPhone = findViewById(R.id.et_contactPhone)
        etContactEmail = findViewById(R.id.et_contactEmail)
        etAddress = findViewById(R.id.et_address)
        val register = findViewById<Button?>(R.id.btn_register)
        val toLogin = findViewById<TextView?>(R.id.tv_toLogin)
        val radioGroup = findViewById<RadioGroup>(R.id.RadioGroup_sex)

        requestGetData()
    }
    private fun requestGetData(){
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String ->
            Log.d("connect", "Response: $response")
            if(response.startsWith("success")){

            }
        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(this, error.toString() , Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "getData"
                data["uid"] = uid
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

}