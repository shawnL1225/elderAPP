package com.example.elderapp.volunteer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
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
import com.example.elderapp.adapter.DiscussComment
import com.example.elderapp.adapter.DiscussCommentAdapter
import com.example.elderapp.adapter.DiscussTitle
import com.example.elderapp.adapter.DiscussTitleAdapter
import org.json.JSONArray
import org.json.JSONException
import java.util.HashMap

class DiscussCommentActivity : AppCompatActivity() , DiscussCommentAdapter.ItemClickListener{
    private var articleId:Int = 0
    var commentList: MutableList<DiscussComment> = ArrayList()
    lateinit var adapter: DiscussCommentAdapter
    lateinit var recyclerView:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discuss_comment)

        val btnBack = findViewById<Button>(R.id.btn_back)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        recyclerView = findViewById(R.id.recycler_discuss)

        btnBack.setOnClickListener { finish() }
        val it = intent
        articleId = it.getIntExtra("article_id", 0)
        tvTitle.text = it.getStringExtra("article_title")

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        requestGetComment()
    }


    private fun requestGetComment() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, Global.url+"discuss.php", Response.Listener { response: String? ->
            Log.d("request", "get discussComment Response: $response")
            commentList.clear()
            try {
                val titles = JSONArray(response)
                for (i in 0 until titles.length()) {
                    val userObj = titles.getJSONObject(i)
                    val id = userObj.getInt("id")
                    val comment = userObj.getString("comment")
                    val uid = userObj.getInt("uid")

                    commentList.add(DiscussComment(id, comment, uid))
                }
                adapter = DiscussCommentAdapter(this, commentList)
                adapter.setClickListener(this)
                recyclerView.adapter = adapter
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error: VolleyError? -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "selectComment"
                data["tid"] = articleId.toString()
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }
}