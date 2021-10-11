package com.example.elderapp


import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.bumptech.glide.Glide
import com.example.elderapp.adapter.Event
import com.example.elderapp.adapter.EventAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONException
import java.util.HashMap


class EventFragment : Fragment(),EventAdapter.ItemClickListener{
    var uid: String? = null
    var url = Global.url+"event.php"
    lateinit var adapter: EventAdapter
    var eventList: MutableList<Event> = ArrayList()
    lateinit var recyclerView: RecyclerView
    var selectMine:Boolean = false
    var isHideAttend:String = "N"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_event, container, false)
        uid = requireActivity().getSharedPreferences("loginUser", AppCompatActivity.MODE_PRIVATE).getString("uid", "")

        recyclerView = view.findViewById(R.id.recycler_event)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        requestEvent()
        val toggleGroup = view.findViewById<MaterialButtonToggleGroup?>(R.id.eventButtonToggleGroup)
        val btnAll = view.findViewById<Button>(R.id.btn_all)
        val btnMine = view.findViewById<Button>(R.id.btn_mine)
        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if(isChecked){
                if (checkedId == R.id.btn_all){
                    btnMine.typeface = Typeface.DEFAULT;
                    btnAll.typeface = Typeface.DEFAULT_BOLD;
                    selectMine = false
                    requestEvent()
                }else if (checkedId == R.id.btn_mine){
                    btnMine.typeface = Typeface.DEFAULT_BOLD;
                    btnAll.typeface = Typeface.DEFAULT;
                    selectMine = true
                    requestEvent()
                }
            }
        }
        val btnAddEvent = view.findViewById<FloatingActionButton>(R.id.btn_addEvent)
        btnAddEvent.setOnClickListener {
            startActivity(Intent(requireContext(), AddEventActivity::class.java))
        }
        return view
    }
    private fun requestEvent(){
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String ->
            Log.d("request", "Response: $response")
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
                    val holderUid = eventObj.getInt("holderUid")
                    val attendeeArr: MutableList<Int> = ArrayList()
                    val attendeeObj = eventObj.getJSONArray("attendee")
                    for (j in 0 until attendeeObj.length()) {
                        attendeeArr.add(attendeeObj[j] as Int)
                    }
                    val status = eventObj.getInt("status")

                    if(!selectMine || attendeeArr.contains(uid!!.toInt())){
                        if(holderUid == uid!!.toInt()) //擁有者至頂
                            eventList.add(0, Event(id, title, location, content, holder, holderUid, date, attendeeArr, status))
                        else
                            eventList.add(Event(id, title, location, content, holder, holderUid, date, attendeeArr, status))
                    }

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
    private fun attend(eid: Int){
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String ->
            Log.d("request", "Response: $response")
            if(response.startsWith("success")){
                requestEvent()
            }

        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "attend"
                data["uid"] = uid
                data["eid"] = eid.toString()
                data["hide"] = isHideAttend
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
    private fun disAttend(eid: Int){
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String ->
            Log.d("request", "Response: $response")
            if(response.startsWith("success")){
                requestEvent()
            }

        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "disAttend"
                data["uid"] = uid
                data["eid"] = eid.toString()
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }
    private fun getNameList(eid: Int, view:TextView){
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String ->
            Log.d("request", "Response: $response")
            view.text = response
        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "getNameList"
                data["eid"] = eid.toString()
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
        var tvDate = view.findViewById<TextView>(R.id.tv_time)
        var tvHolder = view.findViewById<TextView>(R.id.tv_holder)
        var tvCount = view.findViewById<TextView>(R.id.tv_count)
        var tvCheck = view.findViewById<TextView>(R.id.tv_check)
        var tvContent = view.findViewById<TextView>(R.id.tv_content)
        var imgEvent = view.findViewById<ImageView>(R.id.img_event)
        var btnAttend = view.findViewById<Button>(R.id.btn_attend)
        var tvList = view.findViewById<TextView>(R.id.tv_list)
        var switchHide = view.findViewById<Switch>(R.id.swithc_hide)
        tvTitle.text = event.title
        tvLocation.text = event.location
        tvDate.text = event.date
        tvHolder.text = event.holder
        tvCount.text = event.attendee.size.toString()+" 人已參與"
        tvContent.text = event.content
        getNameList(event.id, tvList)


        if(!event.attendee.contains(uid!!.toInt())){
            tvCheck.visibility = View.GONE
            btnAttend.text = "參加"
        }
        else{
            setHideState(event.id, switchHide)
        }


        var imgUrl ="${Global.urlData}/event_img/${event.id}.jpg"
        Glide.with(requireContext())
                .load(imgUrl)
                .centerCrop()
                .into(imgEvent)

        btnAttend.setOnClickListener {
            if(btnAttend.text == "參加"){
                attend(event.id)
                tvCheck.visibility = View.VISIBLE
                btnAttend.text = "取消參加"
                tvCount.text = (event.attendee.size+1).toString()+" 人已參與"
                Handler().postDelayed({
                    getNameList(event.id, tvList)
                    bottomSheetDialog.setContentView(view)
                    bottomSheetDialog.show()
                }, 200)
            }
            else if(btnAttend.text == "取消參加"){
                disAttend(event.id)
                tvCheck.visibility = View.GONE
                btnAttend.text = "參加"
                tvCount.text = (event.attendee.size-1).toString()+" 人已參與"
                Handler().postDelayed({
                    getNameList(event.id, tvList)
                    bottomSheetDialog.setContentView(view)
                    bottomSheetDialog.show()
                }, 200)
            }
        }
        switchHide.setOnCheckedChangeListener { compoundButton, onSwitch ->
            if(onSwitch){
                isHideAttend = "Y"
            }else{
                isHideAttend = "N"
            }
            if(btnAttend.text == "取消參加"){
                disAttend(event.id)
                Handler().postDelayed({
                    attend(event.id)
                }, 200)
                Handler().postDelayed({
                    getNameList(event.id, tvList)
                    bottomSheetDialog.setContentView(view)
                    bottomSheetDialog.show()
                }, 200)


            }
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    private fun setHideState(eid: Int, swt:Switch) {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String ->
            Log.d("switch", "Response: $response")
            if(response == "Y")
                swt.isChecked = true

        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "hideState"
                data["eid"] = eid.toString()
                data["uid"] = uid
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }


    override fun onDeleteClick(position: Int) {
        val builder = AlertDialog.Builder(context)
        val event = adapter.getEvent(position)

        builder.setTitle("刪除活動  -  ${event.title}")
                .setPositiveButton("確定") { _, _ ->  deleteEvent(event.id)}
                .setNegativeButton("取消") { dialogInterface, _ -> dialogInterface.dismiss() }
                .show()
    }

    private fun deleteEvent(eid: Int) {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String ->
            Log.d("request", "Response: $response")
            if(response.startsWith("success")){
                requestEvent()
            }

        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "deleteEvent"
                data["eid"] = eid.toString()
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    override fun onResume() {
        super.onResume()
        requestEvent()
    }


}