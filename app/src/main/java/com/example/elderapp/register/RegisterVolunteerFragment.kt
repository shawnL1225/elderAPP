package com.example.elderapp.register

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.solver.GoalRow
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.elderapp.Global
import com.example.elderapp.LoginActivity
import com.example.elderapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayOutputStream
import java.util.*


class RegisterVolunteerFragment : Fragment() {

    lateinit var imgHeadshot: ImageView
    lateinit var etName: EditText
    lateinit var etPhone: EditText
    lateinit var etPassword: EditText
    lateinit var etPasswordC: EditText
    lateinit var etDepartment: EditText
    lateinit var etEmail: EditText
    lateinit var radioGroup: RadioGroup
    lateinit var name: String
    lateinit var phone: String
    lateinit var pass: String
    lateinit var passC: String
    lateinit var email: String
    lateinit var department: String
    var headshot: String = "default_n"
    private var sex: String? = null
    private val url: String = Global.url+"register.php"
    lateinit var btnCancel: FloatingActionButton
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_register_volunteer, container, false)
        etName = root.findViewById(R.id.et_name)
        etPhone = root.findViewById(R.id.et_phone)
        etPassword = root.findViewById(R.id.et_password)
        etPasswordC = root.findViewById(R.id.et_passwordCheck)
        etDepartment = root.findViewById(R.id.et_department)
        etEmail = root.findViewById(R.id.et_email)
        imgHeadshot = root.findViewById(R.id.img_headshot)
        val register = root.findViewById<Button?>(R.id.btn_register)
        val toLogin = root.findViewById<TextView?>(R.id.tv_toLogin)
        radioGroup = root.findViewById<RadioGroup>(R.id.RadioGroup_sex)
        val upload = root.findViewById<FloatingActionButton?>(R.id.btn_upload)

        upload.setOnClickListener {
            context?.let { context ->
                CropImage.activity().setCropShape(CropImageView.CropShape.OVAL)
                        .setAspectRatio(1, 1)
                        .start(context, this)
            };
        }

        btnCancel = root.findViewById<FloatingActionButton>(R.id.btn_cancel)
        btnCancel.setOnClickListener {
            changeDefaultHead()
            btnCancel.visibility = View.INVISIBLE
        }
        radioGroup.setOnCheckedChangeListener{ radioGroup: RadioGroup, i: Int ->
            if(headshot!!.startsWith("default")){
                changeDefaultHead()
            }
        }


        register.setOnClickListener {
            name = etName.text.toString().trim { it <= ' ' }
            phone = etPhone.text.toString().trim { it <= ' ' }
            pass = etPassword.text.toString().trim { it <= ' ' }
            passC = etPasswordC.text.toString().trim { it <= ' ' }
            email = etEmail.text.toString().trim()
            department = etDepartment.text.toString().trim { it <= ' ' }

            sex = when(radioGroup.checkedRadioButtonId){
                R.id.RadioButton_M ->  "M"
                R.id.RadioButton_F -> "F"
                R.id.RadioButton_N -> "N"
                else -> "_"
            }

            if (pass != passC) {
                Global.putSnackBarR(etName, "密碼不相符")
            } else if (name == "" || phone == "" || email == "" || department == "") {
                Global.putSnackBarR(etName, "請輸入完整資訊")
            } else if(phone!!.length != 10 || !phone.startsWith("09")){
                Global.putSnackBarR(etName!!, "請輸入合法電話號碼")
            } else {
                SQL()
            }
        }
        toLogin.setOnClickListener { startActivity(Intent(context, LoginActivity::class.java)) }
        return root
    }

    private fun SQL() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String ->
            Log.d("request", "Response: $response")
            when {
                response == "isExist" -> {
                    Global.putSnackBarR(etName, "已存在此電話使用者")
                }
                response.startsWith("success") -> {
                    val it = Intent(context, LoginActivity::class.java)
                    it.putExtra("signUp", true)
                    startActivity(it)
                    activity?.finish()
                }
                response.startsWith("failure") -> {
                    Toast.makeText(context, "註冊失敗", Toast.LENGTH_SHORT).show()
                }
            }
        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(context, error.toString().trim { it <= ' ' }, Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["name"] = name
                data["phone"] = phone
                data["password"] = pass
                data["identity"] = "1"
                data["department"] = department
                data["sex"] = sex
                data["email"] = email
                data["headshot"] = headshot
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    private fun changeDefaultHead() {
        val idx = when(radioGroup!!.checkedRadioButtonId){
            R.id.RadioButton_M -> 0
            R.id.RadioButton_F -> 1
            R.id.RadioButton_N -> 2
            else -> 2
        }

        headshot = arrayOf("default_m","default_f","default_n")[idx]
        val res = arrayOf(R.drawable.male_nobg,R.drawable.female_nobg,R.drawable.nonsex)[idx]
        Glide.with(this)
                .load(res)
                .circleCrop()
                .into(imgHeadshot!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var result:CropImage.ActivityResult? = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                var resultUri:Uri = result!!.uri;
                val bitmap = Global.getResizedBitmap(
                        MediaStore.Images.Media.getBitmap(this.context?.contentResolver, resultUri),
                        200
                )

                val baos = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val b = baos.toByteArray()
                val base64 = Base64.encodeToString(b, Base64.DEFAULT)
                Log.d("base64", base64)
                uploadHeadShot(base64)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                var error:Exception = result!!.error;
            }
        }
    }


    private fun uploadHeadShot(base64string : String) {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, Global.url + "imageUpload.php", Response.Listener { response: String ->

            var url ="${Global.urlData}headshot/${response}.jpg"
            Global.putSnackBar(etName, "上傳成功")
            Glide.with(this)
                    .load(url)
                    .circleCrop()
                    .into(imgHeadshot)
            headshot = response
            btnCancel.visibility = View.VISIBLE

        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(context, error.toString().trim { it <= ' ' }, Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["headshot"] = base64string
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
}