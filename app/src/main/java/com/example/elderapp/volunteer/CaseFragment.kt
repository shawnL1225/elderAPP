package com.example.elderapp.volunteer

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.elderapp.R
import com.google.android.material.button.MaterialButtonToggleGroup

/**
 * A simple [Fragment] subclass.
 * Use the [CaseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_case, container, false)
        val navController = Navigation.findNavController(root.findViewById(R.id.fragment))
        val toggleGroup = root.findViewById<MaterialButtonToggleGroup>(R.id.toggleGroup)

        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if(isChecked){
                if (checkedId == R.id.btn_all){
                    navController.navigate(R.id.action_volunteerMyCaseFragment_to_volunteerAllCaseFragment)
                }else if (checkedId == R.id.btn_mine){
                    navController.navigate(R.id.action_volunteerAllCaseFragment_to_volunteerMyCaseFragment)
                }
            }
        }

        // Inflate the layout for this fragment
        return root
    }



}