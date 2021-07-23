package com.example.elderapp.volunteer


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment.STYLE_NORMAL
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.elderapp.Global
import com.example.elderapp.R
import com.example.elderapp.adapter.Event
import com.example.elderapp.adapter.EventAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButtonToggleGroup
import org.json.JSONArray
import org.json.JSONException
import java.util.HashMap


class VtEventFragment : Fragment(),EventAdapter.ItemClickListener{
    var uid: String? = null
    var url = Global.url+"event.php"
    lateinit var adapter: EventAdapter
    var eventList: MutableList<Event> = ArrayList()
    lateinit var recyclerView: RecyclerView
    var selectMine:Boolean = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_event_vt, container, false)
        uid = requireActivity().getSharedPreferences("loginUser", AppCompatActivity.MODE_PRIVATE).getString("uid", "")

        recyclerView = view.findViewById(R.id.recycler_event)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        requestEvent()
        val toggleGroup = view.findViewById<MaterialButtonToggleGroup?>(R.id.eventButtonToggleGroup)
        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if(isChecked){
                if (checkedId == R.id.btn_all){
                    selectMine = false
                    requestEvent()
                }else if (checkedId == R.id.btn_mine){
                    selectMine = true
                    requestEvent()
                }
            }


        }
        return view
    }
    private fun requestEvent(){
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
                    val content = eventObj.getString("content")
                    val holder = eventObj.getString("holder")
                    val date = eventObj.getString("date")
                    val attendeeArr: MutableList<Int> = ArrayList()
                    val attendeeObj = eventObj.getJSONArray("attendee")
                    for (j in 0 until attendeeObj.length()) {
                        attendeeArr.add(attendeeObj[j] as Int)
                    }
                    if(!selectMine || !attendeeArr.contains(uid!!.toInt()))
                        eventList.add(Event(id, title, location, content, holder, date, attendeeArr))
                }
                adapter = EventAdapter(requireContext(), eventList, uid!!.toInt())
                adapter.setClickListener(this)
                recyclerView.adapter = adapter
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "getEvent"

                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }


    override fun onItemClick(position: Int) {
        val event = adapter.getEvent(position)
        val bottomSheetDialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialog)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_event, null)
        var tvTitle = view.findViewById<TextView>(R.id.tv_title)
        var tvLocation = view.findViewById<TextView>(R.id.tv_location)
        var tvDate = view.findViewById<TextView>(R.id.tv_date)
        var tvHolder = view.findViewById<TextView>(R.id.tv_holder)
        var tvCount = view.findViewById<TextView>(R.id.tv_count)
        var tvCheck = view.findViewById<TextView>(R.id.tv_check)
        var tvContent = view.findViewById<TextView>(R.id.tv_content)
        var imgEvent = view.findViewById<ImageView>(R.id.img_event)
        var btnAttend = view.findViewById<Button>(R.id.btn_attend)
        tvTitle.text = event.title
        tvLocation.text = "地點: "+event.location
        tvDate.text = "時間: "+event.date
        tvHolder.text = event.holder
        tvCount.text = event.attendee.size.toString()+" 人已參與"
        tvContent.text = event.content
        if(event.attendee.contains(uid!!.toInt())){
            tvCheck.visibility = View.VISIBLE
        }
        var imgUrl ="${Global.url}event_img/${event.id}.jpg"
        Glide.with(requireContext())
                .load(imgUrl)
                .centerCrop()
                .into(imgEvent)
        btnAttend.setOnClickListener {

        }
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }


}