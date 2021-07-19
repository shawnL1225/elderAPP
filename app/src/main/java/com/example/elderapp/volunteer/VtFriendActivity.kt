package com.example.elderapp.volunteer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.example.elderapp.adapter.FriendAdapter
import com.example.elderapp.adapter.User
import org.json.JSONArray
import org.json.JSONException
import java.util.ArrayList
import java.util.HashMap

class VtFriendActivity : AppCompatActivity() {
    private val url: String = Global.url + "setFriend.php"
    lateinit var adapter: FriendAdapter
    var userList: MutableList<User> = ArrayList()
    lateinit var recyclerView: RecyclerView
    var uid :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vt_friend)

        val btnBack = findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }
        uid = getSharedPreferences("loginUser", MODE_PRIVATE).getString("uid", "")
        recyclerView = findViewById(R.id.recycler_vt_friend)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.itemAnimator
        requestGetFriends();
    }

    private fun requestGetFriends() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String? ->
            Log.d("connect", "select Response: $response")
            userList.clear()
            try {
                val users = JSONArray(response)
                for (i in 0 until users.length()) {
                    val userObj = users.getJSONObject(i)
                    val id = userObj.getInt("id")
                    val name = userObj.getString("name")
                    val phone = userObj.getString("phone")

                    userList.add(User(id, name, phone))
                }
                adapter = FriendAdapter(userList)
//                adapter.setClickListener(this)
                recyclerView.adapter = adapter
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error: VolleyError? -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "select-volunteer"
                data["uid"] = uid
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}