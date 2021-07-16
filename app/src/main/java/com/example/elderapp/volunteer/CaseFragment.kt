package com.example.elderapp.volunteer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.elderapp.R

/**
 * A simple [Fragment] subclass.
 * Use the [CaseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_case, container, false)
    }



}