package com.example.elderapp.elder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.elderapp.R


class ElderFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_elder, container, false)
        val btnEvent = view.findViewById<LinearLayout>(R.id.btn_event)
        val btnGoOut = view.findViewById<LinearLayout>(R.id.btn_goOut)
        val btnCall = view.findViewById<LinearLayout>(R.id.btn_call)
        val btnTodo = view.findViewById<LinearLayout>(R.id.btn_todo)
        btnEvent.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_elderFragment_to_edEventFragment) }
        btnGoOut.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_elderFragment_to_goOutFragment) }
        btnTodo.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_elderFragment_to_todoFragment) }
        btnCall.setOnClickListener {

        }
        return view
    }


}