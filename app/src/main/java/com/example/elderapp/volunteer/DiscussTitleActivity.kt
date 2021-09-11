package com.example.elderapp.volunteer

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import java.util.ArrayList
import java.util.HashMap

class DiscussTitleActivity : AppCompatActivity(), DiscussTitleAdapter.ItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private var titleList: MutableList<DiscussTitle> = ArrayList()
    private lateinit var adapter: DiscussTitleAdapter
    private  lateinit var uid :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discuss_title)

        uid = getSharedPreferences("loginUser", MODE_PRIVATE).getString("uid", "")!!
        val btnBack = findViewById<Button>(R.id.btn_back)
        val btnAddPost = findViewById<ExtendedFloatingActionButton>(R.id.btn_add_post)
        btnBack.setOnClickListener { finish() }
        btnAddPost.setOnClickListener {
            addPost()
        }
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
                adapter = DiscussTitleAdapter(this, titleList, uid.toInt())
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

    override fun onDeleteClick(position: Int) {
        val discuss = adapter.getDiscuss(position)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("確定要刪除此貼文")
            .setPositiveButton("確定") { _, _ -> deletePost(discuss.id) }
            .setNegativeButton("取消") { dialogInterface, _ -> dialogInterface.dismiss() }
            .show()
    }

    private fun deletePost(tid:Int) {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, Global.url+"discuss.php", Response.Listener { response: String? ->
            Log.d("request", "delete discussTitle Response: $response")
            if(response!!.startsWith("success")){
                Global.putSnackBar(recyclerView, "成功刪除貼文")
                requestGetTitle()
            }

        }, Response.ErrorListener { error: VolleyError? -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "deletePost"
                data["tid"] = tid.toString()
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun addPost(){
        val builder = AlertDialog.Builder(this)
        val dialog = LayoutInflater.from(this).inflate(R.layout.dialog_add_todo, null)
        val etContent = dialog.findViewById<EditText>(R.id.et_content)
        var content = ""
        builder.setTitle("新增貼文").setView(dialog)
            .setPositiveButton("確認") { dialogInterface: DialogInterface, i: Int ->
                content = etContent.text.toString().trim()
                if(content == "") {
                    Global.putSnackBarR(recyclerView, "需填寫貼文標題")
                    return@setPositiveButton
                }
                requestStoreTitle(content)

            }
            .setNegativeButton("取消") { _, _-> }
            .show()
    }

    private fun requestStoreTitle(title:String) {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, Global.url+"discuss.php", Response.Listener { response: String? ->
            Log.d("request", "store discussTitle Response: $response")
            if(response!!.startsWith("success")){
                Global.putSnackBar(recyclerView, "成功新增貼文")
                requestGetTitle()
            }

        }, Response.ErrorListener { error: VolleyError? -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "storeTitle"
                data["title"] = title
                data["uid"] = uid
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}