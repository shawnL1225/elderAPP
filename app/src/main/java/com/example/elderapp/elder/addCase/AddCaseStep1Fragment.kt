package com.example.elderapp.elder.addCase

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.elderapp.Global
import com.example.elderapp.R
import com.example.elderapp.adapter.Place
import com.example.elderapp.adapter.PlaceForCaseAdapter
import com.example.elderapp.elder.EditPlaceActivity
import com.google.gson.Gson
import org.json.JSONException
import java.util.HashMap
import java.util.Observer

class AddCaseStep1Fragment : Fragment() {

    var uid :Int = 0;

    private val viewModel: AddCaseViewModel by activityViewModels()
    lateinit var list_place:RecyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.add_case_step1_fragment, container, false)
        uid = requireContext().getSharedPreferences("loginUser", AppCompatActivity.MODE_PRIVATE).getString("uid", "0")?.toInt()
                ?: -1
        list_place = root.findViewById(R.id.list_place)
        val btnSet = root.findViewById<Button>(R.id.btn_set)

        getPlaces(){
            list_place.layoutManager = LinearLayoutManager(requireContext())
            list_place.adapter = PlaceForCaseAdapter(it.toMutableList()){place->
                 viewModel.setPlace(place.id) //(activity as EdAddCaseActivity?)!!.createCase()
                (activity as EdAddCaseActivity?)!!.navController.navigate(R.id.action_addCaseStep1Fragment_to_addCaseStep2Fragment)
            }
        }
        btnSet.setOnClickListener {
            startActivity(Intent(requireContext(), EditPlaceActivity::class.java))
        }
        return root
    }

    private fun getPlaces( callback: (Array<Place>) -> Unit) {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, "${Global.url}setPlace.php", Response.Listener { response: String? ->
            try {
                callback(Gson().fromJson(response, Array<Place>::class.java))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error: VolleyError? -> Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["select"] = ""
                data["uid"] = uid.toString()
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

    override fun onResume() {
        super.onResume()
        getPlaces(){
            list_place.layoutManager = LinearLayoutManager(requireContext())
            list_place.adapter = PlaceForCaseAdapter(it.toMutableList()){place->
                viewModel.setPlace(place.id) //(activity as EdAddCaseActivity?)!!.createCase()
                (activity as EdAddCaseActivity?)!!.navController.navigate(R.id.action_addCaseStep1Fragment_to_addCaseStep2Fragment)
            }
        }
    }
}