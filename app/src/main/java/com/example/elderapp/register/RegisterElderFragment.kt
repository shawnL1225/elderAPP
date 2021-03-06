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
    private var headshot: String = "default_n"

    private val url: String? = Global.url+"register.php"
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

//        radioGroup.setOnCheckedChangeListener{ radioGroup: RadioGroup, i: Int ->
//                val idx = when(radioGroup.checkedRadioButtonId){
//                    R.id.RadioButton_M -> 0
//                    R.id.RadioButton_F -> 1
//                    R.id.RadioButton_N -> 2
//                    else -> 2
//                }
//
//                headshot = arrayOf("default_m","default_f","default_n")[idx]
//                val res = arrayOf(R.drawable.male,R.drawable.female,R.drawable.nonsex)[idx]
//
//                Log.d("headshot",headshot)
//        }

        register.setOnClickListener {
            name = etName!!.text.toString().trim()
            phone = etPhone!!.text.toString().trim()
            pass = etPassword!!.text.toString().trim()
            passC = etPasswordC!!.text.toString().trim ()
            remarksIll = etRemarksIll!!.text.toString().trim()
            remarksEating = etRemarksEating!!.text.toString().trim()
            remarksOther = etRemarksOther!!.text.toString().trim()
            contactPhone = etContactPhone!!.text.toString().trim()
            contactEmail = etContactEmail!!.text.toString().trim()
            addr = etAddress!!.text.toString().trim()
            when(radioGroup.checkedRadioButtonId){
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
                Global.putSnackBarR(etName!!, "???????????????")
            } else if (name == "" || phone == "" || contactPhone == "") {
                Global.putSnackBarR(etName!!, "?????????????????????")
            } else if(phone!!.length != 10 || contactPhone!!.length!=10 || !phone!!.startsWith("09") || !contactPhone!!.startsWith("09") ){
                Global.putSnackBarR(etName!!, "???????????????????????????")
            } else {
                SQL()
            }
        }
        toLogin.setOnClickListener { startActivity(Intent(context, LoginActivity::class.java)) }
        return root
    }

    fun SQL() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String ->
            Log.d("request", "Response: $response")
            when {
                response == "isExist" -> {
                    Global.putSnackBarR(etName!!, "???????????????????????????")
                }
                response.startsWith("success") -> {
                    val it = Intent(context, LoginActivity::class.java)
                    it.putExtra("signUp", true)
                    startActivity(it)
                    activity?.finish()
                }
                response.startsWith("failure") -> {
                    Toast.makeText(context, "????????????", Toast.LENGTH_SHORT).show()
                }
            }
        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["name"] = name
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
                data["headshot"] = headshot

                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
}