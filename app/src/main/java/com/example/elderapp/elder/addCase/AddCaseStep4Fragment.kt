package com.example.elderapp.elder.addCase
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import com.example.elderapp.R
import java.text.SimpleDateFormat
import java.util.*

class AddCaseStep4Fragment : Fragment() {

    private val viewModel: AddCaseViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.add_case_step4_fragment, container, false)
        root.findViewById<Button>(R.id.btn_30).setOnClickListener {
            setPublic(30)
        }
        root.findViewById<Button>(R.id.btn_1h).setOnClickListener {
            setPublic(60)
        }
        root.findViewById<Button>(R.id.btn_3h).setOnClickListener {
            setPublic(180)
        }
        return root
    }


    fun setPublic(time: Int){
        val now = Calendar.getInstance();
        now.add(Calendar.MINUTE,time)
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        viewModel.setPublic(formatter.format(now.time))
        (activity as EdAddCaseActivity?)!!.createCase()
    }
}