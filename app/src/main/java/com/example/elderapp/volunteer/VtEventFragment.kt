package com.example.elderapp.volunteer


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.elderapp.Global
import com.example.elderapp.R
import com.example.elderapp.adapter.Event
import com.example.elderapp.adapter.EventAdapter
import com.google.android.material.button.MaterialButtonToggleGroup
import org.json.JSONArray
import org.json.JSONException
import java.util.HashMap


class VtEventFragment : Fragment() {
    var uid: String? = null
    var url = Global.url+"event.php"
    lateinit var adapter: EventAdapter
    var eventList: MutableList<Event> = ArrayList()
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_event_vt, container, false)
        uid = requireActivity().getSharedPreferences("loginUser", AppCompatActivity.MODE_PRIVATE).getString("uid", "")

        recyclerView = view.findViewById(R.id.recycler_event)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        requestEventAll()
        val toggleGroup = view.findViewById<MaterialButtonToggleGroup?>(R.id.eventButtonToggleGroup)
        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if(isChecked){
                if (checkedId == R.id.btn_all){
                    Log.d("button", "ALL")
                    requestEventAll()
                }else if (checkedId == R.id.btn_mine){
                    requestEventMine()
                    Log.d("button", "MINE")
                }
            }


        }
        return view
    }
    private fun requestEventAll(){
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String ->
            Log.d("connect", "all Response: $response")
            eventList.clear()
            try {
                val events = JSONArray(response)
                for (i in 0 until events.length()) {
                    val eventObj = events.getJSONObject(i)
                    val id = eventObj.getInt("id")
                    val title = eventObj.getString("title")
                    val location = eventObj.getString("location")
                    val description = eventObj.getString("description")
                    val holder = eventObj.getString("holder")
                    val date = eventObj.getString("date")
                    val attendeeArr: MutableList<Int> = ArrayList()
                    val attendeeObj = eventObj.getJSONArray("attendee")
                    for (j in 0 until attendeeObj.length()) {
                        attendeeArr.add(attendeeObj[j] as Int)
                    }

                    eventList.add(Event(id, title, location, description, holder, date, attendeeArr))
                }
                adapter = EventAdapter(eventList)
//                adapter.setClickListener(this)
                recyclerView.adapter = adapter
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "getEventAll"

                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
    private fun requestEventMine(){
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String ->
            Log.d("connect", "mine Response: $response")
            eventList.clear()
            try {
                val events = JSONArray(response)
                for (i in 0 until events.length()) {
                    val eventObj = events.getJSONObject(i)
                    val id = eventObj.getInt("id")
                    val title = eventObj.getString("title")
                    val location = eventObj.getString("location")
                    val description = eventObj.getString("description")
                    val holder = eventObj.getString("holder")
                    val date = eventObj.getString("date")
                    val attendeeArr: MutableList<Int> = ArrayList()
                    val attendeeObj = eventObj.getJSONArray("attendee")
                    for (j in 0 until attendeeObj.length()) {
                        attendeeArr.add(attendeeObj[j] as Int)
                    }

                    eventList.add(Event(id, title, location, description, holder, date, attendeeArr))
                }

                adapter = EventAdapter(eventList)
//                adapter.setClickListener(this)
                recyclerView.adapter = adapter
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "getEventMine"
                data["uid"] = uid
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }


}