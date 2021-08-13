package com.example.elderapp.elder.addCase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import com.example.elderapp.R
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class AddCaseStep2Fragment : Fragment() {

    companion object {
        fun newInstance() = AddCaseStep2Fragment()
    }

    private val viewModel: AddCaseViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.add_case_step2_fragment, container, false)

        root.findViewById<Button>(R.id.btn_select_date).setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("選擇日期")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            datePicker.show(requireActivity().supportFragmentManager, "timePicker");
            datePicker.addOnPositiveButtonClickListener { date->
                val timePicker = MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H )
                        .setTitleText("選擇今天的時間")
                        .build()
                timePicker.show(requireActivity().supportFragmentManager, "timePicker");
                timePicker.addOnPositiveButtonClickListener {
                    val now = Date(date)
                    now.hours = timePicker.hour
                    now.minutes = timePicker.minute
                    now.seconds = 0
                    setDate(now)
                }
            }
        }

        root.findViewById<Button>(R.id.btn_today).setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H )
                    .setTitleText("選擇今天的時間")
                    .build()
            timePicker.show(requireActivity().supportFragmentManager, "timePicker");
            timePicker.addOnPositiveButtonClickListener {
                val now = Calendar.getInstance().time
                now.hours = timePicker.hour
                now.minutes = timePicker.minute
                now.seconds = 0
                setDate(now)
            }
        }

        root.findViewById<Button>(R.id.btn_now).setOnClickListener {
            setDate(Calendar.getInstance().time)
        }

        return root
    }

    private fun setDate(date:Date){
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        viewModel.setDate(formatter.format(date))
        (activity as EdAddCaseActivity?)!!.navController.navigate(R.id.action_addCaseStep2Fragment_to_addCaseStep3Fragment)
    }

}