package com.example.elderapp.elder

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.elderapp.Global
import com.example.elderapp.R
import com.example.elderapp.adapter.Place
import com.example.elderapp.adapter.PlaceAdapter
import com.example.elderapp.adapter.PlaceAdapter.ItemClickListener
import com.google.android.material.button.MaterialButtonToggleGroup
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class EditPlaceActivity : AppCompatActivity(), ItemClickListener {
    private val url: String = Global.url + "setPlace.php"
    lateinit var adapter: PlaceAdapter
    var placeList: MutableList<Place> = ArrayList()
    lateinit var recyclerView: RecyclerView
    var uid :String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_place)

        uid = getSharedPreferences("loginUser", MODE_PRIVATE).getString("uid", "")
        recyclerView = findViewById(R.id.recycler_place)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        SQL_getPlaces()

        val btnBack = findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onItemClick(position: Int) {
        val builder = AlertDialog.Builder(this)
        val title = adapter.getTitle(position)
        val placeId = adapter.getId(position)
        builder.setTitle("刪除地點  -  $title")
                .setPositiveButton("確定") { _, _ -> SQL_deletePlace(placeId) }
                .setNegativeButton("取消") { dialogInterface, _ -> dialogInterface.dismiss() }
                .show()
        //        Toast.makeText(this, "You clicked " + adapter.getTitle(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    private fun SQL_deletePlace(placeId: Int) {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String? ->
            Log.d("connect", "delete Response: $response")
            if (response!!.startsWith("success")) {
                Global.putSnackBar(recyclerView, "成功刪除地點")
                SQL_getPlaces()
            }
        }, Response.ErrorListener { error: VolleyError? -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["delete"] = ""
                data["id"] = placeId.toString()
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun SQL_getPlaces() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String? ->
            Log.d("connect", "select Response: $response")
            placeList.clear()
            try {
                val places = JSONArray(response)
                for (i in 0 until places.length()) {
                    val placeObj = places.getJSONObject(i)
                    val title = placeObj.getString("title")
                    val description = placeObj.getString("description")
                    val id = placeObj.getInt("id")
                    val iconID = placeObj.getInt("iconID")
                    placeList.add(Place(title, description, id, iconID))
                }
                adapter = PlaceAdapter(placeList)
                adapter.setClickListener(this)
                recyclerView.adapter = adapter
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error: VolleyError? -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["select"] = ""
                data["uid"] = uid
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    var etPlaceTitle: EditText? = null
    var etPlaceDesc: EditText? = null
    var insTitle: String? = null
    var insDesc: String? = null
    var iconID: String? = null
    fun addPlace(view: View) {  //floating button
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_place, null)
        etPlaceDesc = dialogView.findViewById(R.id.et_placeDesc)
        etPlaceTitle = dialogView.findViewById(R.id.et_placeTitle)
        val toggleGroup = dialogView.findViewById<MaterialButtonToggleGroup?>(R.id.tg_placeIcon)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView).setTitle("新增地點")
                .setPositiveButton("確定") { _, _ ->
                    insTitle = etPlaceTitle?.text.toString().trim()
                    insDesc = etPlaceDesc?.text.toString().trim()
                    when (toggleGroup.checkedButtonId) {
                        R.id.btn_icon1 -> iconID = "1"
                        R.id.btn_icon2 -> iconID = "2"
                        R.id.btn_icon3 -> iconID = "3"
                        R.id.btn_icon4 -> iconID = "4"
                        R.id.btn_icon5 -> iconID = "5"
                    }

                    if (insTitle != "") {
                        SQL_storePlace()
                    } else {
                        Global.putSnackBarR(recyclerView, "請輸入地點名稱")
                    }
                }
                .setNegativeButton("取消") { _, _-> }
                .show()
    }

    private fun SQL_storePlace() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String ->
            Log.d("connect", "store Response: $response")
            if (response.startsWith("success")) {
                SQL_getPlaces()
                Global.putSnackBar(recyclerView, "已設置地點")
            } else if (response.startsWith("failure")) {
                Toast.makeText(this@EditPlaceActivity, "儲存失敗", Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["insert"] = ""
                data["title"] = insTitle
                data["desc"] = insDesc
                data["uid"] = uid
                data["iconID"] = iconID
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}