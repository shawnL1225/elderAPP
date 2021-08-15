package com.example.elderapp.volunteer

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.elderapp.Global
import com.example.elderapp.LoginActivity
import com.example.elderapp.R
import com.example.elderapp.RawUser
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayOutputStream
import java.util.HashMap

class VtProfileActivity : AppCompatActivity() {
    private var etName: EditText? = null
    private var etPhone: EditText? = null
    private var etPassword: EditText? = null
    private var etPasswordC: EditText? = null
    private var etDepartment: EditText? = null
    private var etEmail: EditText? = null
    private var name: String? = null
    private var phone: String? = null
    private var pass: String? = ""
    private var passC: String? = ""
    private var department: String? = ""
    private var email: String? = ""
    private var sex: String? = ""
    private var uid: String? = ""
    private var radioGroup: RadioGroup? = null
    private var headshot: String? = "default_n"
    private var imgHeadshot: ImageView? = null
    lateinit var btnCancel: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vt_profile)

        uid = getSharedPreferences("loginUser", MODE_PRIVATE).getString("uid", "")
        etName = findViewById(R.id.et_name)
        etPhone = findViewById(R.id.et_phone)
        etDepartment = findViewById(R.id.et_department)
        etEmail = findViewById(R.id.et_email)
        radioGroup = findViewById(R.id.RadioGroup_sex)
        imgHeadshot = findViewById(R.id.img_headshot)

        val btnBack = findViewById<Button>(R.id.btn_back)
        val btnUpdate = findViewById<Button>(R.id.btn_update)
        val upload = findViewById<FloatingActionButton?>(R.id.btn_upload)
        val btnRepass = findViewById<Button>(R.id.btn_repass)
        btnCancel = findViewById<FloatingActionButton>(R.id.btn_cancel)
        val btnDeleteUser = findViewById<Button>(R.id.btn_delete_user)
        btnCancel.setOnClickListener {
            changeDefaultHead()
            btnCancel.visibility = View.INVISIBLE
        }
        radioGroup!!.setOnCheckedChangeListener{ radioGroup: RadioGroup, i: Int ->
            if(headshot!!.startsWith("default")){
                changeDefaultHead()
            }
        }
        btnBack.setOnClickListener {
            finish()
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
        btnUpdate.setOnClickListener {
            name = etName!!.text.toString().trim()
            phone = etPhone!!.text.toString().trim()
            pass = etPassword!!.text.toString().trim()
            passC = etPasswordC!!.text.toString().trim ()
            email = etEmail!!.text.toString().trim()
            department = etDepartment!!.text.toString().trim ()

            when(radioGroup!!.checkedRadioButtonId){
                R.id.RadioButton_M -> sex = "M"
                R.id.RadioButton_F -> sex = "F"
                R.id.RadioButton_N -> sex = "N"
            }

            if (pass != passC) {
                Global.putSnackBarR(etName!!, "密碼不相符")
            } else if (name == "" || phone == "" || department == "") {
                Global.putSnackBarR(etName!!, "請輸入完整資訊")
            } else {
                updateProfile()
            }
        }
        btnDeleteUser.setOnClickListener {
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("確定要刪除此帳戶")
                    .setPositiveButton("確定") { _, _ -> deleteUser() }
                    .setNegativeButton("取消") { dialogInterface, _ -> dialogInterface.dismiss() }
                    .show()

        }
        upload.setOnClickListener {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setAspectRatio(1, 1)
                    .start(this)

        }

        requestGetData()

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
    private fun updateProfile() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, Global.url+"setProfile.php", Response.Listener { response: String ->
            Log.d("request", "Response: $response")
            if (response.startsWith("success")) {
                startActivity(Intent(this, VolunteerActivity::class.java))
                finish()
            } else if (response.startsWith("failure")) {
                Toast.makeText(this, "更新失敗", Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(this, error.toString().trim { it <= ' ' }, Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "updateVolunteer"
                data["uid"] = uid
                data["name"] = name
                data["phone"] = phone
                data["department"] = department
                data["sex"] = sex
                data["email"] = email
                data["headshot"] = headshot
                data["password"] = pass

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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var result:CropImage.ActivityResult? = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                var resultUri: Uri = result!!.uri;
                val bitmap = Global.getResizedBitmap(
                        MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri),
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

            var url ="${Global.url}headshot/${response}.jpg"
            Glide.with(this)
                    .load(url)
                    .circleCrop()
                    .into(imgHeadshot!!)
            Global.putSnackBar(etName!!, "上傳成功")

            headshot = response
            btnCancel.visibility = View.VISIBLE

        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(this, error.toString().trim { it <= ' ' }, Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["headshot"] = base64string
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
    private fun requestGetData(){
        Global.profile(this, uid!!){ user: RawUser ->
            etPhone!!.setText(user.phone)
            etName!!.setText(user.name)
            etPhone!!.setText(user.phone)
            etDepartment!!.setText(user.department)
            etEmail!!.setText(user.email)
            headshot = user.headshot
            Global.headUp(this, imgHeadshot!!, user.headshot)
            when(user.sex){
                "M" -> radioGroup!!.check(R.id.RadioButton_M)
                "F" -> radioGroup!!.check(R.id.RadioButton_F)
                "N" -> radioGroup!!.check(R.id.RadioButton_N)
            }
            if(!user.headshot.startsWith("default"))
                btnCancel.visibility = View.VISIBLE

        }
    }
}