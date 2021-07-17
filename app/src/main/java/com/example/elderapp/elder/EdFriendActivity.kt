package com.example.elderapp.elder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.elderapp.Global
import com.example.elderapp.R
import com.example.elderapp.adapter.User
import com.example.elderapp.adapter.FriendAdapter
import org.json.JSONArray
import org.json.JSONException
import java.util.ArrayList
import java.util.HashMap

class EdFriendActivity : AppCompatActivity(), FriendAdapter.ItemClickListener {
    private val url: String = Global.url + "setFriend.php"
    lateinit var adapter: FriendAdapter
    var userList: MutableList<User> = ArrayList()
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ed_friend)

        val btnBack = findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }
        recyclerView = findViewById(R.id.recycler_ed_friend)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        requestGetPlaces();
    }

    fun addFriend(view: View) {

    }

    private fun requestGetPlaces() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String? ->
            Log.d("connect", "select Response: $response")
            userList.clear()
            try {
                val places = JSONArray(response)
                for (i in 0 until places.length()) {
                    val placeObj = places.getJSONObject(i)
                    val id = placeObj.getInt("id")
                    val name = placeObj.getString("name")
                    val phone = placeObj.getString("phone")

                    userList.add(User(id, name, phone))
                }
                adapter = FriendAdapter(userList)
                adapter.setClickListener(this)
                recyclerView.adapter = adapter
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error: VolleyError? -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                val uid = getSharedPreferences("mySP", MODE_PRIVATE).getString("uid", "")
                data["type"] = "select"
                data["uid"] = uid
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    override fun onItemClick(position: Int) {

    }

}