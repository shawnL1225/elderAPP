package com.example.elderapp.volunteer

import android.app.AlertDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.elderapp.Global
import com.example.elderapp.R
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*


class HourRecordFragment : Fragment() {

    lateinit var tvDate:TextView
    lateinit var tvTime:TextView
    lateinit var etHour:EditText
    lateinit var etContent:EditText
    private var date:String = ""
    private var time:String = ""
    private var hour:String = ""
    private var content:String = ""
    private var uid:String? = ""
    lateinit var checkBox1:CheckBox
    lateinit var checkBox2:CheckBox
    lateinit var checkBox3:CheckBox
    lateinit var checkBox4:CheckBox
    lateinit var checkBox5:CheckBox
    lateinit var checkBox6:CheckBox



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_hour_record, container, false)

        uid = requireActivity().getSharedPreferences("loginUser", AppCompatActivity.MODE_PRIVATE).getString("uid", "")
        val btnDate = root.findViewById<Button>(R.id.btn_date)
        val btnTime = root.findViewById<Button>(R.id.btn_time)
        val btnFinish = root.findViewById<Button>(R.id.btn_finish)
        tvDate = root.findViewById(R.id.tv_date)
        tvTime = root.findViewById(R.id.tv_time)
        etHour = root.findViewById(R.id.et_hour)
        etContent = root.findViewById(R.id.et_content)
        checkBox1 = root.findViewById(R.id.checkBox1)
        checkBox2 = root.findViewById(R.id.checkBox2)
        checkBox3 = root.findViewById(R.id.checkBox3)
        checkBox4 = root.findViewById(R.id.checkBox4)
        checkBox5 = root.findViewById(R.id.checkBox5)
        checkBox6 = root.findViewById(R.id.checkBox6)

        btnDate.setOnClickListener {
            openDatePicker()
        }
        btnTime.setOnClickListener {
            openStartTimePicker()
        }
        btnFinish.setOnClickListener {
            hour = etHour.text.toString().trim()
            if(checkBox1.isChecked) content = '*'+checkBox1.text.toString()
            if(checkBox2.isChecked) content += '*'+checkBox2.text.toString()
            if(checkBox3.isChecked) content += '*'+checkBox3.text.toString()
            if(checkBox4.isChecked) content += '*'+checkBox4.text.toString()
            if(checkBox5.isChecked) content += '*'+checkBox5.text.toString()
            if(checkBox6.isChecked) content += '*'+checkBox6.text.toString()
            content += "\n"+etContent.text.toString().trim()
            if(hour == "" || time == "" || date == "" || content == ""){
                Global.putSnackBarR(tvDate, "?????????????????????")
                return@setOnClickListener
            }
            val builder = AlertDialog.Builder(requireContext())
            val layout  = LinearLayout(requireContext())
            val textView  = TextView(requireContext())
            textView.text = "??????: $date\n??????: $time\n??????: $hour\n??????: $content"
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            layout.addView(textView)
            layout.setPadding(80,15,80,15)
            builder.setView(layout).setTitle("???????????????????????????")
                    .setPositiveButton("??????") { _, _ ->
                        uploadHour()
                    }
                    .setNegativeButton("??????") { _, _-> }
                    .show()


        }

        return root


    }

    private fun uploadHour() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, Global.url+"hourRecord.php", Response.Listener { response: String ->
            Log.d("request", "uploadHour: $response")
            if(response.startsWith("success")) {
                Global.putSnackBar(tvDate, "??????????????????")

            }
            else
                Toast.makeText(requireContext(), "????????????" , Toast.LENGTH_SHORT).show()

        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(requireContext(), error.toString() , Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "uploadHour"
                data["date"] = date
                data["time"] = time
                data["hour"] = hour
                data["content"] = content
                data["uid"] = uid

                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

    private fun openStartTimePicker() {
        val clockFormat = TimeFormat.CLOCK_24H
        val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .setHour(12)
                .setMinute(10)
                .setTitleText("??????????????????")
                .build()
        timePicker.show(parentFragmentManager, "timePicker");
        timePicker.addOnPositiveButtonClickListener {
            time = timePicker.hour.toString()+":"
            if( timePicker.minute < 10) time += '0'
            time += timePicker.minute.toString()
            openEndTimePicker()
        }

    }
    private fun openEndTimePicker() {
        val clockFormat = TimeFormat.CLOCK_24H
        val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .setHour(12)
                .setMinute(10)
                .setTitleText("??????????????????")
                .build()
        timePicker.show(parentFragmentManager, "timePicker");
        timePicker.addOnPositiveButtonClickListener {

            time += " ~ "+timePicker.hour.toString()+":"
            if( timePicker.minute < 10) time += '0'
            time += timePicker.minute.toString()
            tvTime.text = time
        }
    }
    private fun openDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("????????????")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        datePicker.show(parentFragmentManager, "timePicker");
        datePicker.addOnPositiveButtonClickListener {
            // Create calendar object and set the date to be that returned from selection
            val calendar = Calendar.getInstance()
            calendar.time = Date(it)
            date = "${calendar.get(Calendar.YEAR)}-" +
                    "${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)} "


            tvDate.text = date
        }
    }

}