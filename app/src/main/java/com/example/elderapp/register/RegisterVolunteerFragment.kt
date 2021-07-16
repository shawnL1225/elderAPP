package com.example.elderapp.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.elderapp.Global
import com.example.elderapp.LoginActivity
import com.example.elderapp.R
import java.util.*

class RegisterVolunteerFragment : Fragment() {
    lateinit var etName: EditText
    lateinit var etPhone: EditText
    lateinit var etPassword: EditText
    lateinit var etPasswordC: EditText
    lateinit var etDepartment: EditText
    lateinit var username: String
    lateinit var phone: String
    lateinit var pass: String
    lateinit var passC: String
    lateinit var department: String
    private var sex: String? = null
    private val url: String = "https://www2.cs.ccu.edu.tw/~lwx109u/elderApp/register.php"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_register_volunteer, container, false)
        etName = root.findViewById(R.id.et_name)
        etPhone = root.findViewById(R.id.et_phone)
        etPassword = root.findViewById(R.id.et_password)
        etPasswordC = root.findViewById(R.id.et_passwordCheck)
        etDepartment = root.findViewById(R.id.et_department)
        val register = root.findViewById<Button?>(R.id.btn_register)
        val toLogin = root.findViewById<TextView?>(R.id.tv_toLogin)
        val radioGroup = root.findViewById<RadioGroup>(R.id.RadioGroup_sex)

        register.setOnClickListener {
            username = etName.text.toString().trim { it <= ' ' }
            phone = etPhone.text.toString().trim { it <= ' ' }
            pass = etPassword.text.toString().trim { it <= ' ' }
            passC = etPasswordC.text.toString().trim { it <= ' ' }
            department = etDepartment.text.toString().trim { it <= ' ' }
            sex = root.findViewById<RadioButton>(radioGroup.checkedRadioButtonId).text.toString()

            if (pass != passC) {
                Global.putSnackBarR(etName, "密碼不相符")
            } else if (username == "" || phone == "") {
                Global.putSnackBarR(etName, "請輸入完整資訊")
            } else {
                SQL()
            }
        }
        toLogin.setOnClickListener { startActivity(Intent(context, LoginActivity::class.java)) }
        return root
    }

    private fun SQL() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String ->
            Log.d("connect", "Response: $response")
            if (response.startsWith("success")) {
                val it = Intent(context, LoginActivity::class.java)
                it.putExtra("signUp", true)
                startActivity(it)
                activity?.finish()
            } else if (response.startsWith("failure")) {
                Toast.makeText(context, "註冊失敗", Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(context, error.toString().trim { it <= ' ' }, Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["username"] = username
                data["phone"] = phone
                data["password"] = pass
                data["identity"] = "1"
                data["department"] = department
                data["sex"] = sex
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
}