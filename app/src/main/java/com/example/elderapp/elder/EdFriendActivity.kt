package com.example.elderapp.elder

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
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
import com.google.android.material.button.MaterialButtonToggleGroup
import org.json.JSONArray
import org.json.JSONException
import java.util.ArrayList
import java.util.HashMap

class EdFriendActivity : AppCompatActivity(), FriendAdapter.ItemClickListener {
    private val url: String = Global.url + "setFriend.php"
    lateinit var adapter: FriendAdapter
    var userList: MutableList<User> = ArrayList()
    lateinit var recyclerView: RecyclerView
    var uid :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ed_friend)

        val btnBack = findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }
        uid = getSharedPreferences("loginUser", MODE_PRIVATE).getString("uid", "")
        recyclerView = findViewById(R.id.recycler_ed_friend)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.itemAnimator
        requestGetFriends();
    }
    override fun onItemClick(position: Int) {
        val builder = AlertDialog.Builder(this)
        val name = adapter.getName(position)
        val friendId = adapter.getId(position)
        builder.setTitle("刪除朋友  -  $name")
                .setPositiveButton("確定") { _, _ -> requestDeleteFriend(friendId) }
                .setNegativeButton("取消") { dialogInterface, _ -> dialogInterface.dismiss() }
                .show()
    }



    private fun requestGetFriends() {
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
                data["type"] = "select"
                data["uid"] = uid
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }




    private fun requestDeleteFriend(friendId: Int) {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String? ->
            Log.d("connect", "delete Response: $response")
            if (response!!.startsWith("success")) {
                Global.putSnackBar(recyclerView, "成功刪除志工朋友")
               requestGetFriends()
            }
        }, Response.ErrorListener { error: VolleyError? -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "delete"
                data["volunteerID"] = friendId.toString()
                data["elderID"] = uid.toString()
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    var etPhone :EditText? = null
    var insPhone :String? = null
    fun addFriend(view: View) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_friend, null)
        etPhone = dialogView.findViewById(R.id.et_phone)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView).setTitle("新增志工朋友")
                .setPositiveButton("確定") { _, _ ->
                    insPhone = etPhone!!.text.toString().trim()
                    if (insPhone!="") {
                        requestStoreFriend()
                    } else {
                        Global.putSnackBarR(recyclerView, "請輸入完整志工電話")
                    }
                }
                .setNegativeButton("取消") { _, _-> }
                .show()
    }

    private fun requestStoreFriend() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response: String ->
            Log.d("connect", "store Response: $response")
            when {
                response.startsWith("success") -> {
                    requestGetFriends()
                    Global.putSnackBar(recyclerView, "已新增志工好友")
                }
                response == "NoUser" -> {
                    Global.putSnackBarR(recyclerView, "查無使用者")
                }
                response == "Exist" -> {
                    Global.putSnackBarR(recyclerView, "使用者已存在")
                }
            }
        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()

                data["type"] = "insert"
                data["elderID"] = uid
                data["phone"] = insPhone
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

}