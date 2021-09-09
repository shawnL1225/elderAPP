package com.example.elderapp.elder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Debug
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.elderapp.Global
import com.example.elderapp.R
import com.example.elderapp.RawUser
import com.example.elderapp.adapter.Case
import com.example.elderapp.adapter.ElderCaseAdapter
import com.example.elderapp.elder.addCase.EdAddCaseActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import org.json.JSONException
import java.util.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_go_out_main.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_go_out_main : Fragment() {
    lateinit var root:View;
    lateinit var tvEmpty:TextView
    var uid:Int = -1;
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        root = inflater.inflate(R.layout.fragment_go_out_main, container, false)
        uid = requireContext().getSharedPreferences("loginUser", AppCompatActivity.MODE_PRIVATE).getString("uid", "0")?.toInt()
                ?: -1
        root.findViewById<FloatingActionButton>(R.id.btn_addCase).setOnClickListener {
            startActivity(Intent(requireContext(),EdAddCaseActivity::class.java))
        }
        tvEmpty = root.findViewById(R.id.tv_empty)
        return root
    }

    override fun onResume() {
        super.onResume()
        loadCases()
    }



    fun loadCases(){
        getList(requireContext()){
            val list_case = root!!.findViewById<RecyclerView>(R.id.list_case)
            list_case.layoutManager = LinearLayoutManager(requireContext())
            list_case.adapter = ElderCaseAdapter(requireContext(), it.toMutableList(),{id:Int -> cancelCase(requireContext(),id)},{id:Int -> cancelReceiver(requireContext(),id)})
            if(it.isNotEmpty()){
                tvEmpty.visibility = View.GONE
            }else{
                tvEmpty.visibility = View.VISIBLE
            }
        }
    }

    fun getList(context: Context, callback: (Array<Case>) -> Unit) {

        val stringRequest: StringRequest = object : StringRequest(Method.POST, Global.url + "/case/list.php", Response.Listener { response: String ->
            try {
                val gson = Gson()
                Log.d("RESSSS",response);
                callback(gson.fromJson(response, Array<Case>::class.java))
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["uid"] = uid.toString();
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    fun cancelCase(context: Context, id:Int) {

        val stringRequest: StringRequest = object : StringRequest(Method.POST, Global.url + "/case/cancel.php", Response.Listener { response: String ->
            Log.d("connect", "Response: $response")
            try {
                if(response == "ok"){
                    Toast.makeText(context,"已取消行程",Toast.LENGTH_SHORT).show()
                    loadCases()
                }else{
                    Toast.makeText(context,"發生錯誤",Toast.LENGTH_SHORT).show()
                    loadCases()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["id"] = id.toString();
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    fun cancelReceiver(context: Context, id:Int) {

        val stringRequest: StringRequest = object : StringRequest(Method.POST, Global.url + "/case/cancelReceiver.php", Response.Listener { response: String ->
            Log.d("connect", "Response: $response")
            try {
                if(response == "ok"){
                    Toast.makeText(context,"已婉拒",Toast.LENGTH_SHORT).show()
                    loadCases()
                }else{
                    Toast.makeText(context,"發生錯誤",Toast.LENGTH_SHORT).show()
                    loadCases()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["id"] = id.toString();
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

}