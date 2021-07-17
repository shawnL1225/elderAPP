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

class RegisterElderFragment : Fragment() {
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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_register_elder, container, false)
        etName = root.findViewById(R.id.et_name)
        etPhone = root.findViewById(R.id.et_phone)
        etPassword = root.findViewById(R.id.et_password)
        etPasswordC = root.findViewById(R.id.et_passwordCheck)
        etRemarksIll = root.findViewById(R.id.et_remarks1)
        etRemarksEating = root.findViewById(R.id.et_remarks2)
        etRemarksOther = root.findViewById(R.id.et_remarks3)
        etContactPhone = root.findViewById(R.id.et_contactPhone)
        etContactEmail = root.findViewById(R.id.et_contactEmail)
        etAddress = root.findViewById(R.id.et_address)
        val register = root.findViewById<Button?>(R.id.btn_register)
        val toLogin = root.findViewById<TextView?>(R.id.tv_toLogin)
        val radioGroup = root.findViewById<RadioGroup>(R.id.RadioGroup_sex)

        register.setOnClickListener {
            username = etName!!.text.toString().trim { it <= ' ' }
            phone = etPhone!!.text.toString().trim { it <= ' ' }
            pass = etPassword!!.text.toString().trim { it <= ' ' }
            passC = etPasswordC!!.text.toString().trim { it <= ' ' }
            remarksIll = etRemarksIll!!.text.toString().trim { it <= ' ' }
            remarksEating = etRemarksEating!!.text.toString().trim { it <= ' ' }
            remarksOther = etRemarksOther!!.text.toString().trim { it <= ' ' }
            contactPhone = etContactPhone!!.text.toString().trim { it <= ' ' }
            contactEmail = etContactEmail!!.text.toString().trim { it <= ' ' }

            addr = etAddress!!.text.toString().trim()
            when(radioGroup.checkedRadioButtonId){
                R.id.RadioButton_M -> sex = "M"
                R.id.RadioButton_F -> sex = "F"
                R.id.RadioButton_N -> sex = "N"
            }

            if (pass != passC) {
                Global.putSnackBarR(etName!!, "密碼不相符")
            } else if (username == "" || phone == "" || contactPhone == "") {
                Global.putSnackBarR(etName!!, "請輸入完整資訊")
            } else {
                SQL()
            }
        }
        toLogin.setOnClickListener { startActivity(Intent(context, LoginActivity::class.java)) }
        return root
    }

    fun SQL() {
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
        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["username"] = username
                data["phone"] = phone
                data["password"] = pass
                data["identity"] = "0"
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
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
}