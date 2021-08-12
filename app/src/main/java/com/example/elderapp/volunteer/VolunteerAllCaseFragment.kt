package com.example.elderapp.volunteer

import android.content.Context
import android.os.Bundle
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
import com.example.elderapp.adapter.Case
import com.example.elderapp.adapter.VolunteerCaseAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VolunteerAllCaseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VolunteerAllCaseFragment : Fragment() {

    var uid:Int = -1
    var root:View? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid = requireContext().getSharedPreferences("loginUser", AppCompatActivity.MODE_PRIVATE).getString("uid", "0")?.toInt()
                ?: -1

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_volunteer_all_case, container, false)
        load_cases(requireContext(),root!!)
        return root
    }

    fun load_cases( context: Context,root: View){

        val all_list = root.findViewById<RecyclerView>(R.id.all_list)
        val invited_list = root.findViewById<RecyclerView>(R.id.case_list)

        getList(context){ res ->


            all_list.layoutManager = LinearLayoutManager(context)
            all_list.adapter =
                    VolunteerCaseAdapter(context,res.filter{it.public!=null && SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(it.public).before(Calendar.getInstance().time) && it.receiver == null && it.invited.find{ i->  i.id == uid} ==null}.toMutableList()).
                    setClickListener(){
                        showCase(it)
                    }

            invited_list.layoutManager = LinearLayoutManager(context)
            invited_list.adapter =
                    VolunteerCaseAdapter(context,res.filter {it.receiver == null && it.invited.find{ i->  i.id == uid} !==null }.toMutableList()).
                            setClickListener(){
                                showCase(it)
                            }

            invited_list.adapter
        }
    }

    fun getList(context: Context, callback: (Array<Case>) -> Unit) {

        val stringRequest: StringRequest = object : StringRequest(Method.POST, Global.url + "/case/list.php", Response.Listener { response: String ->
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
                       Toast.makeText(requireContext(),"已將工作加入\"我的工作\"",Toast.LENGTH_SHORT).show()
                       load_cases(requireContext(),root!!)
                   }else{
                       Toast.makeText(requireContext(),"發生錯誤，請稍後再試",Toast.LENGTH_SHORT).show()
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

}