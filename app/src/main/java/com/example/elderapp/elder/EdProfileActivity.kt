package com.example.elderapp.elder

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.elderapp.Global
import com.example.elderapp.LoginActivity
import com.example.elderapp.R
import com.example.elderapp.RawUser
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
    private var pass: String? = ""
    private var passC: String? = ""
    private var remarksIll: String? = ""
    private var remarksEating: String? = ""
    private var remarksOther: String? = ""
    private var contactPhone: String? = ""
    private var contactEmail: String? = ""
    private var sex: String? = ""
    private var addr: String? = ""
    private var headshot: String? = ""
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
        radioGroup = findViewById(R.id.RadioGroup_sex)
        val btnUpdate = findViewById<Button>(R.id.btn_update)
        val btnBack = findViewById<Button>(R.id.btn_back)
        val btnRepass = findViewById<Button>(R.id.btn_repass)
        val btnDeleteUser = findViewById<Button>(R.id.btn_delete_user)
        requestGetData()
        btnUpdate.setOnClickListener {
            name = etName!!.text.toString().trim()
            phone = etPhone!!.text.toString().trim()
            remarksIll = etRemarksIll!!.text.toString().trim()
            remarksEating = etRemarksEating!!.text.toString().trim()
            remarksOther = etRemarksOther!!.text.toString().trim()
            contactPhone = etContactPhone!!.text.toString().trim()
            contactEmail = etContactEmail!!.text.toString().trim()
            addr = etAddress!!.text.toString().trim()
            when(radioGroup!!.checkedRadioButtonId){
                R.id.RadioButton_M -> {
                    sex = "M"; headshot = "default_m"
                }
                R.id.RadioButton_F -> {
                    sex = "F"; headshot = "default_f"
                }
                R.id.RadioButton_N -> {
                    sex = "N"; headshot = "default_n"
                }
            }

            if (pass != passC) {
                Global.putSnackBarR(etName!!, "密碼不相符")
            } else if (name == "" || phone == "" || contactPhone == "") {
                Global.putSnackBarR(etName!!, "請輸入完整資訊")
            } else {
                requestUpdateData()
            }
        }
        btnDeleteUser.setOnClickListener {
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("確定要刪除此帳戶")
                    .setPositiveButton("確定") { _, _ -> deleteUser() }
                    .setNegativeButton("取消") { dialogInterface, _ -> dialogInterface.dismiss() }
                    .show()

        }
        btnRepass.setOnClickListener {
            val dialogPass = LayoutInflater.from(this).inflate(R.layout.dialog_new_password, null)
            etPassword = dialogPass.findViewById(R.id.et_password)
            etPasswordC = dialogPass.findViewById(R.id.et_password_c)
            val builder = AlertDialog.Builder(this)
            builder.setView(dialogPass).setTitle("修改密碼")
                    .setPositiveButton("確認") { dialogInterface: DialogInterface, i: Int ->
                        pass = etPassword!!.text.toString().trim()
                        passC = etPasswordC!!.text.toString().trim()
                        if(pass != passC) Global.putSnackBarR(etPassword!!, "密碼不相同")
                        else updatePassword()
                    }
                    .setNegativeButton("取消") { _, _-> }
                    .show()
        }
        btnBack.setOnClickListener { finish() }

    }
    private fun deleteUser() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, Global.url+"setProfile.php", Response.Listener { response: String ->
            Log.d("request", "Response: $response")
            if (response.startsWith("success")) {
                Toast.makeText(this, "成功刪除使用者", Toast.LENGTH_SHORT).show()
                getSharedPreferences("loginUser", MODE_PRIVATE)
                        .edit().putString("uid", "").apply()
                startActivity(Intent(this, LoginActivity::class.java))

            } else if (response.startsWith("failure")) {
                Toast.makeText(this, "刪除失敗", Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(this, error.toString().trim { it <= ' ' }, Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "deleteUser"
                data["uid"] = uid
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
    private fun requestUpdateData() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String ->
            Log.d("request", "Response: $response")
            if (response.startsWith("success")) {
                Global.putSnackBar(etName!!,"更新成功!")
                requestGetData()

            } else if (response.startsWith("failure")) {
                Global.putSnackBarR(etName!!,"沒有資料有更動")
            }

        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(this, error.toString() , Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "updateElder"
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
                data["headshot"] = headshot
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
    private fun updatePassword() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, Global.url+"setProfile.php", Response.Listener { response: String ->
            Log.d("request", "Response: $response")
            if (response.startsWith("success")) {
                Global.putSnackBar(etName!!, "更新密碼成功")
            } else if (response.startsWith("failure")) {
                Toast.makeText(this, "更新失敗", Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(this, error.toString().trim { it <= ' ' }, Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "updatePassword"
                data["uid"] = uid
                data["password"] = pass

                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
    private fun requestGetData(){
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String ->
            Log.d("request", "Response: $response")
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