package com.example.elderapp.elder

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.elderapp.Global
import com.example.elderapp.R
import com.example.elderapp.adapter.Case
import com.example.elderapp.adapter.ElderCaseAdapter
import com.example.elderapp.adapter.ElderCaseMemberAdapter
import com.google.gson.Gson
import org.json.JSONException
import java.util.HashMap


class GoOutFragment : Fragment() {
    var root:View? = null;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        root = inflater.inflate(R.layout.fragment_go_out, container, false)
        return root
    }


}