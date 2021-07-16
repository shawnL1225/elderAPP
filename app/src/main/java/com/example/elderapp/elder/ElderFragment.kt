package com.example.elderapp.elder

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.elderapp.EditPlaceActivity
import com.example.elderapp.R


class ElderFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_elder, container, false)
        view.findViewById<View?>(R.id.btn_editPlace).setOnClickListener { v: View? -> startActivity(Intent(context, EditPlaceActivity::class.java)) }


//        view.findViewById(R.id.ibtn_to_do).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(v).navigate(R.id.action_elderFragment_to_todoFragment);
//            }
//        });
//
//        view.findViewById(R.id.ibtn_go_out).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(v).navigate(R.id.action_elderFragment_to_goOutFragment);
//            }
//        });
//
//        view.findViewById(R.id.ibtn_event_ed).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(v).navigate(R.id.action_elderFragment_to_edEventFragment);
//            }
//        });
        return view
    }


}