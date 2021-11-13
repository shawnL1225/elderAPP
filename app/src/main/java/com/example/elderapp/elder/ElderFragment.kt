package com.example.elderapp.elder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.elderapp.Global
import com.example.elderapp.R
import com.example.elderapp.RawUser


class ElderFragment : Fragment() {
    private var callNumber :String = "0900000000"
    private var uid :String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_elder, container, false)
        val btnEvent = view.findViewById<LinearLayout>(R.id.btn_back2)
        val btnGoOut = view.findViewById<LinearLayout>(R.id.btn_goOut)
        val btnCall = view.findViewById<LinearLayout>(R.id.btn_call)
        val btnTodo = view.findViewById<LinearLayout>(R.id.btn_todo)
        val btnBack = activity?.findViewById<Button>(R.id.btn_back)
        btnEvent.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_elderFragment_to_eventFragment)
            btnBack?.visibility = View.VISIBLE
        }
        btnGoOut.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_elderFragment_to_goOutFragment)
            btnBack?.visibility = View.VISIBLE
        }
        btnTodo.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_elderFragment_to_todoFragment)
            btnBack?.visibility = View.VISIBLE
        }

        uid = requireActivity().getSharedPreferences("loginUser", AppCompatActivity.MODE_PRIVATE).getString("uid", "")
        Global.profile(requireContext(),uid!!){ user: RawUser ->
            callNumber = user.contactPhone
        }
        btnCall.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        arrayOf(Manifest.permission.CALL_PHONE), 2)
            }else{
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$callNumber"))
                startActivity(intent)
            }

        }
        return view
    }


}