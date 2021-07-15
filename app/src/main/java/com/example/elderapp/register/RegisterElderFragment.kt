package com.example.elderapp.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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

class RegisterElderFragment : Fragment() {
    private var etName: EditText? = null
    private var etPhone: EditText? = null
    private var etPassword: EditText? = null
    private var etPasswordC: EditText? = null
    private var etRemarks_ill: EditText? = null
    private var etRemarks_eating: EditText? = null
    private var etRemarks_other: EditText? = null
    private var etContactPhone: EditText? = null
    private var etContactEmail: EditText? = null
    private val url: String? = "https://www2.cs.ccu.edu.tw/~lwx109u/elderApp/register.php"
    private var username: String? = null
    private var phone: String? = null
    private var pass: String? = null
    private var passC: String? = null
    private var remarks_ill: String? = ""
    private var remarks_eating: String? = ""
    private var remarks_other: String? = ""
    private var contactPhone: String? = null
    private var contactEmail: String? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_register_elder, container, false)
        etName = root.findViewById(R.id.et_name)
        etPhone = root.findViewById(R.id.et_phone)
        etPassword = root.findViewById(R.id.et_password)
        etPasswordC = root.findViewById(R.id.et_passwordCheck)
        etRemarks_ill = root.findViewById(R.id.et_remarks1)
        etRemarks_eating = root.findViewById(R.id.et_remarks2)
        etRemarks_other = root.findViewById(R.id.et_remarks3)
        etContactPhone = root.findViewById(R.id.et_contactPhone)
        etContactEmail = root.findViewById(R.id.et_contactEmail)
        val register = root.findViewById<Button?>(R.id.btn_register)
        val toLogin = root.findViewById<TextView?>(R.id.tv_toLogin)
        register.setOnClickListener { view: View? ->
            username = etName.getText().toString().trim { it <= ' ' }
            phone = etPhone.getText().toString().trim { it <= ' ' }
            pass = etPassword.getText().toString().trim { it <= ' ' }
            passC = etPasswordC.getText().toString().trim { it <= ' ' }
            remarks_ill = etRemarks_ill.getText().toString().trim { it <= ' ' }
            remarks_eating = etRemarks_eating.getText().toString().trim { it <= ' ' }
            remarks_other = etRemarks_other.getText().toString().trim { it <= ' ' }
            contactPhone = etContactPhone.getText().toString().trim { it <= ' ' }
            contactEmail = etContactEmail.getText().toString().trim { it <= ' ' }
            if (pass != passC) {
                Global.putSnackBarR(etName, "密碼不相符")
            } else if (username == "" || phone == "" || contactPhone == "") {
                Global.putSnackBarR(etName, "請輸入完整資訊")
            } else {
                SQL()
            }
        }
        toLogin.setOnClickListener { view: View? -> startActivity(Intent(context, LoginActivity::class.java)) }
        return root
    }

    fun SQL() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String? ->
            Log.d("connect", "Response: $response")
            if (response.startsWith("success")) {
                val it = Intent(context, LoginActivity::class.java)
                it.putExtra("signUp", true)
                startActivity(it)
                activity.finish()
            } else if (response.startsWith("failure")) {
                Toast.makeText(context, "註冊失敗", Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error: VolleyError? -> Toast.makeText(context, error.toString().trim { it <= ' ' }, Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String?, String?>? {
                val data: MutableMap<String?, String?> = HashMap()
                data["username"] = username
                data["phone"] = phone
                data["password"] = pass
                data["identity"] = "0"
                data["remarks_ill"] = remarks_ill
                data["remarks_eating"] = remarks_eating
                data["remarks_other"] = remarks_other
                data["contactPhone"] = contactPhone
                data["contactEmail"] = contactEmail
                Log.d("connect", remarks_eating)
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
}