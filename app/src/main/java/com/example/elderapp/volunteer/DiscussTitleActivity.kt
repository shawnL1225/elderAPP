package com.example.elderapp.volunteer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
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
import com.example.elderapp.adapter.*
import org.json.JSONArray
import org.json.JSONException
import java.util.ArrayList
import java.util.HashMap

class DiscussTitleActivity : AppCompatActivity(), DiscussTitleAdapter.ItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private var titleList: MutableList<DiscussTitle> = ArrayList()
    private lateinit var adapter: DiscussTitleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discuss_title)

        val btnBack = findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener { finish() }
        requestGetTitle()
        recyclerView = findViewById(R.id.recycler_discuss)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun requestGetTitle() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, Global.url+"discuss.php", Response.Listener { response: String? ->
            Log.d("request", "get discussTitle Response: $response")
            titleList.clear()
            try {
                val titles = JSONArray(response)
                for (i in 0 until titles.length()) {
                    val userObj = titles.getJSONObject(i)
                    val id = userObj.getInt("id")
                    val title = userObj.getString("title")
                    val uid = userObj.getInt("uid")

                    titleList.add(DiscussTitle(id, title, uid))
                }
                adapter = DiscussTitleAdapter(this, titleList)
                adapter.setClickListener(this)
                recyclerView.adapter = adapter
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error: VolleyError? -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "selectTitle"
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    override fun onItemClick(position: Int) {
        val discuss = adapter.getDiscuss(position)
        val it = Intent(this, DiscussCommentActivity::class.java)
        it.putExtra("article_id",discuss.id)
        it.putExtra("article_title",discuss.title)
        startActivity(it)
    }
}