package com.example.elderapp

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.DateFormat.is24HourFormat
import android.util.Base64
import android.util.Log
import android.util.TypedValue
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayOutputStream
import java.util.*


class AddEventActivity : AppCompatActivity() {
    lateinit var uid:String
    lateinit var imgEvent:ImageView
    lateinit var tvDate: TextView
    var uploadImg:String = ""
    lateinit var title:String
    lateinit var location:String
    lateinit var content:String
    lateinit var holder: String
    var date:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        uid = getSharedPreferences("loginUser", MODE_PRIVATE).getString("uid", "")!!
        val btnBack = findViewById<Button>(R.id.btn_back)
        val btnUpload = findViewById<FloatingActionButton>(R.id.btn_upload)
        val btnTime = findViewById<Button>(R.id.btn_date)
        val btnFinish = findViewById<Button>(R.id.btn_finish)
        tvDate = findViewById<TextView>(R.id.tv_time)
        val etTitle = findViewById<EditText>(R.id.et_title)
        val etLocation = findViewById<EditText>(R.id.et_location)
        val etContent = findViewById<EditText>(R.id.et_content)
        val radioGroup = findViewById<RadioGroup>(R.id.RadioGroup)
        imgEvent = findViewById<ImageView>(R.id.img_event)
        btnBack.setOnClickListener { finish() }

        btnUpload.setOnClickListener {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setAspectRatio(1, 1)
                    .start(this)
        }
        btnTime.setOnClickListener {
            openDatePicker()
        }


        btnFinish.setOnClickListener {
            title = etTitle.text.toString().trim()
            location = etLocation.text.toString().trim()
            content = etContent.text.toString().trim()
            if(uploadImg == ""){
                Global.putSnackBarR(imgEvent, "?????????????????????")
                return@setOnClickListener
            }else if(date == "" || title == "" || location == "" || content == ""){
                Global.putSnackBarR(imgEvent, "???????????????????????????")
                return@setOnClickListener
            }

            holder = when(radioGroup.checkedRadioButtonId){
                R.id.holder -> "?????????: "
                R.id.sharer -> "?????????: "
                else -> ""
            }
            val name = getSharedPreferences("loginUser", MODE_PRIVATE).getString("name", "")!!
            holder += name

            val builder = AlertDialog.Builder(this)
            val layout  = LinearLayout(this)
            val textView  = TextView(this)
            textView.text = "??????: $title\n??????: $location\n??????: $content\n$holder"
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            layout.addView(textView)
            layout.setPadding(80,15,80,15)
            builder.setView(layout).setTitle("???????????????????????????\n???????????????????????????")
                .setPositiveButton("??????") { _, _ ->
                    requestAddEvent()
                }
                .setNegativeButton("??????") { _, _-> }
                .show()

        }

    }

    private fun requestAddEvent() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, Global.url+"event.php", Response.Listener { response: String ->
            Log.d("request", "Response: $response")
            if (response.startsWith("success")) {
                Toast.makeText(this, "???????????????????????????????????????", Toast.LENGTH_SHORT).show()
                finish()
            } else if (response.startsWith("failure")) {
                Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show()
            }

        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(this, error.toString() , Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "addEvent"
                data["title"] = title
                data["location"] = location
                data["content"] = content
                data["holder"] = holder
                data["date"] = date
                data["img"] = uploadImg
                data["uid"] = uid

                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
    private fun openTimePicker() {
        val clockFormat = TimeFormat.CLOCK_24H
        val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .setHour(12)
                .setMinute(10)
                .setTitleText("????????????")
                .build()
        timePicker.show(supportFragmentManager, "timePicker");
        timePicker.addOnPositiveButtonClickListener {
            date += timePicker.hour.toString()+":"
            if( timePicker.minute < 10) date += '0'
            date += timePicker.minute.toString()
            tvDate.text = date
        }
    }
    private fun openDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("????????????")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build()
        datePicker.show(supportFragmentManager, "timePicker");
        datePicker.addOnPositiveButtonClickListener {
            // Create calendar object and set the date to be that returned from selection
            val calendar = Calendar.getInstance()

            calendar.time = Date(it)
            if(calendar.before(Calendar.getInstance())){
                Global.putSnackBarR(tvDate!!,"?????????????????????")
                return@addOnPositiveButtonClickListener
            }
            date = "${calendar.get(Calendar.YEAR)}-" +
                    "${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)} "


            tvDate.text = date
            openTimePicker()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var result:CropImage.ActivityResult? = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                var resultUri:Uri = result!!.uri;
                val bitmap = Global.getResizedBitmap(
                        MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri),
                        200
                )
                Glide.with(this)
                        .load(bitmap)
                        .circleCrop()
                        .into(imgEvent)

                val baos = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val b = baos.toByteArray()
                val base64 = Base64.encodeToString(b, Base64.DEFAULT)
                Log.d("base64", base64)
                uploadImg = base64
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                var error:Exception = result!!.error;
            }
        }
    }



}