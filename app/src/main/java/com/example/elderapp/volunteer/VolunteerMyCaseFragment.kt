package com.example.elderapp.volunteer

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.example.elderapp.adapter.VolunteerCaseAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.HashMap

class VolunteerMyCaseFragment : Fragment() {

    var uid:Int = -1
    var root:View? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid = requireContext().getSharedPreferences("loginUser", AppCompatActivity.MODE_PRIVATE).getString("uid", "0")?.toInt()
                ?: -1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_volunteer_my_case, container, false)
        load_cases(requireContext(),root!!)

        return root
    }

    fun load_cases(context: Context, root: View){

        val case_list = root.findViewById<RecyclerView>(R.id.case_list)

            getList(context) { res ->

                val uid = context.getSharedPreferences("loginUser", AppCompatActivity.MODE_PRIVATE).getString("uid", "0")

                case_list.layoutManager = LinearLayoutManager(context)
                case_list.adapter = VolunteerCaseAdapter(context, res.filter { it.receiver?.id ?: 0 == uid?.toInt() ?: -1 }.toMutableList()).setClickListener() {
                    showCase(it)
                }
            }

    }

    fun getList(context: Context, callback: (Array<Case>) -> Unit) {

        val stringRequest: StringRequest = object : StringRequest(Method.POST, Global.url + "/case/list.php", Response.Listener { response: String ->
            Log.d("connect", "Response: $response")
            try {
                val gson = Gson()
                callback(gson.fromJson(response, Array<Case>::class.java))
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }, Response.ErrorListener { error: VolleyError -> Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }



    fun showCase(case:Case){
        val bottomSheetDialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialog)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_case, null)
        view.findViewById<TextView>(R.id.txt_submitter).text = case.submitter.name
        Global.headUp(requireContext(),view.findViewById<ImageView>(R.id.img_headshot),case.submitter.headshot)

        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formatter = SimpleDateFormat("MM-dd HH:mm")
        val date = formatter.format(parser.parse(case.date))

        view.findViewById<TextView>(R.id.txt_date).text = date
        view.findViewById<TextView>(R.id.txt_place).text = case.place.title

        val txt_received=  view.findViewById<TextView>(R.id.txt_received)
        val btn_receive=  view.findViewById<TextView>(R.id.btn_receive)

        if(case.receiver == null){
            txt_received.visibility = View.GONE
            btn_receive.visibility = View.VISIBLE
            btn_receive.setOnClickListener {
                val stringRequest: StringRequest = object : StringRequest(Method.POST, Global.url + "/case/receive.php", Response.Listener { response: String ->
                    bottomSheetDialog.hide()
                    if(response=="ok"){
                        Toast.makeText(requireContext(),"??????????????????\"????????????\"",Toast.LENGTH_SHORT).show()
                        load_cases(requireContext(),root!!)
                    }else{
                        Toast.makeText(requireContext(),"??????????????????????????????",Toast.LENGTH_SHORT).show()
                        load_cases(requireContext(),root!!)
                    }
                }, Response.ErrorListener { error: VolleyError -> Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show() }) {
                    override fun getParams(): MutableMap<String?, String?> {
                        val data: MutableMap<String?, String?> = HashMap()
                        data["case_id"] = case.id.toString()
                        data["uid"] = uid.toString()
                        return data
                    }
                }
                val requestQueue = Volley.newRequestQueue(context)
                requestQueue.add(stringRequest)
            }
        }else if(case.receiver!!.id == uid){
            txt_received.visibility = View.VISIBLE
            btn_receive.visibility = View.GONE
        }else{
            txt_received.visibility = View.GONE
            btn_receive.visibility = View.GONE
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }


    fun getSex(context: Context, callback: (String) -> Unit) {
        Global.profile(context, uid.toString()) { user: RawUser ->
            callback(user.sex)
        }
    }
}