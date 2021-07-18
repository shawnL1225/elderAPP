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
import com.example.elderapp.Global
import com.example.elderapp.LoginActivity
import com.example.elderapp.R
import org.json.JSONException
import org.json.JSONObject
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
    private var name: String? = null
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
    private var radioGroup: RadioGroup? = null
    private val url: String? = Global.url+"setProfile.php"
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
        radioGroup = findViewById<RadioGroup>(R.id.RadioGroup_sex)
        val btnUpdate = findViewById<Button>(R.id.btn_update)
        btnUpdate.setOnClickListener {
            requestUpdateData()
        }

        requestGetData()
    }

    private fun requestUpdateData() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String ->
            Log.d("connect", "Response: $response")
            if (response.startsWith("success")) {
                Global.putSnackBar(etName!!,"更新成功")

            } else if (response.startsWith("failure")) {
                Toast.makeText(this, "更新失敗", Toast.LENGTH_SHORT).show()
            }
            
        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(this, error.toString() , Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "updateData"
                data["uid"] = uid
                data["name"] = name
                data["phone"] = phone
                data["password"] = pass
                data["remarks_ill"] = remarksIll
                data["remarks_eating"] = remarksEating
                data["remarks_other"] = remarksOther
                data["contactPhone"] = contactPhone
                data["contactEmail"] = contactEmail
                data["address"] = addr
                data["sex"] = sex
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun requestGetData(){
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String ->
            Log.d("connect", "Response: $response")
            try {
                val userObj = JSONObject(response)
                name = userObj.getString("name")
                phone = userObj.getString("phone")
                remarksIll = userObj.getString("remarks_illness")
                remarksEating = userObj.getString("remarks_eating")
                remarksOther = userObj.getString("remarks_other")
                contactPhone = userObj.getString("contactPhone")
                contactEmail = userObj.getString("contactEmail")
                sex = userObj.getString("sex")
                addr = userObj.getString("address")
                etName!!.setText(name)
                etPhone!!.setText(phone)
                etRemarksIll!!.setText(remarksIll)
                etRemarksEating!!.setText(remarksEating)
                etRemarksOther!!.setText(remarksOther)
                etContactPhone!!.setText(contactPhone)
                etContactEmail!!.setText(contactEmail)
                etAddress!!.setText(addr)
                when(sex){
                    "M" -> radioGroup!!.check(R.id.RadioButton_M)
                    "F" -> radioGroup!!.check(R.id.RadioButton_F)
                    "N" -> radioGroup!!.check(R.id.RadioButton_N)

                }


            }catch (e: JSONException) {
                e.printStackTrace()
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