package com.example.elderapp.volunteer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
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
import org.json.JSONArray
import org.json.JSONException
import java.util.HashMap
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog


class DiscussCommentActivity : AppCompatActivity() , DiscussCommentAdapter.ItemClickListener{
    private var articleId:Int = 0
    var commentList: MutableList<DiscussComment> = ArrayList()
    lateinit var adapter: DiscussCommentAdapter
    lateinit var recyclerView:RecyclerView
    lateinit var etAddComment:EditText
    lateinit var comment :String
    lateinit var uid :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discuss_comment)

        uid = getSharedPreferences("loginUser", MODE_PRIVATE).getString("uid", "")!!
        val btnBack = findViewById<Button>(R.id.btn_back)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        recyclerView = findViewById(R.id.recycler_discuss)
        etAddComment = findViewById(R.id.et_add_comment)
        val btnSubmit = findViewById<ImageView>(R.id.btn_submit)
        btnSubmit.setOnClickListener {
            comment = etAddComment.text.toString().trim()
            if(comment != ""){
                storeComment()
            }
        }
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
                adapter = DiscussCommentAdapter(this, commentList, uid.toInt())
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

    override fun onEditClick(position: Int) {
        val comment = adapter.getComment(position)
        val builder = AlertDialog.Builder(this)
        val dialog = LayoutInflater.from(this).inflate(R.layout.dialog_add_todo, null)
        val etContent = dialog.findViewById<EditText>(R.id.et_content)
        var content = ""
        builder.setTitle("編輯留言").setView(dialog)
            .setPositiveButton("確認") { dialogInterface: DialogInterface, i: Int ->
                content = etContent.text.toString().trim()
                if(content == "") {
                    Global.putSnackBarR(recyclerView, "需填寫留言內容")
                    return@setPositiveButton
                }
                editComment(comment.id, content)

            }
            .setNegativeButton("取消") { _, _-> }
            .show()
    }

    override fun onDeleteClick(position: Int) {
        val comment = adapter.getComment(position)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("確定要刪除此留言")
            .setPositiveButton("確定") { _, _ -> deleteComment(comment.id) }
            .setNegativeButton("取消") { dialogInterface, _ -> dialogInterface.dismiss() }
            .show()
    }

    private fun deleteComment(id: Int) {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, Global.url+"discuss.php", Response.Listener { response: String? ->
            Log.d("request", "delete discussComment Response: $response")
            if(response!!.startsWith("success")){
                Global.putSnackBar(recyclerView, "成功刪除留言")
                requestGetComment()
            }

        }, Response.ErrorListener { error: VolleyError? -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "deleteComment"
                data["id"] = id.toString()
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun editComment(id: Int, comment:String) {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, Global.url+"discuss.php", Response.Listener { response: String? ->
            Log.d("request", "edit discussComment Response: $response")
            if(response!!.startsWith("success")){
                Global.putSnackBar(recyclerView, "成功編輯留言")
                requestGetComment()
            }

        }, Response.ErrorListener { error: VolleyError? -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "editComment"
                data["comment"] = comment
                data["id"] = id.toString()
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun storeComment() {
        val stringRequest: StringRequest = object : StringRequest(Method.POST, Global.url+"discuss.php", Response.Listener { response: String? ->
            Log.d("request", "store discussComment Response: $response")
            if(response!!.startsWith("success")){
                requestGetComment()
                etAddComment.setText("")
                this.currentFocus?.let { view ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }

        }, Response.ErrorListener { error: VolleyError? -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "storeComment"
                data["tid"] = articleId.toString()
                data["uid"] = uid
                data["comment"] = comment
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}