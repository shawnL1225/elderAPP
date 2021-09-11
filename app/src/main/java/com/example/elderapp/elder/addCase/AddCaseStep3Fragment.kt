package com.example.elderapp.elder.addCase
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.elderapp.Global
import com.example.elderapp.R
import com.example.elderapp.adapter.*
import com.google.gson.Gson
import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.*

class AddCaseStep3Fragment : Fragment() {

    lateinit var uid:Number;
    lateinit var adapter:FriendForCaseAdapter;

    private val viewModel: AddCaseViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.add_case_step3_fragment, container, false)
        uid = requireContext().getSharedPreferences("loginUser", AppCompatActivity.MODE_PRIVATE).getString("uid", "0")?.toInt()
                ?: -1

        val list_friend = root.findViewById<RecyclerView>(R.id.list_friend)
        val check_public = root.findViewById<CheckBox>(R.id.check_public)
        val sex_limit = root.findViewById<LinearLayout>(R.id.sex_limit)
        val RadioGroup_sex = root.findViewById<RadioGroup>(R.id.RadioGroup_sex)

        getFriends(){
            list_friend.layoutManager = LinearLayoutManager(requireContext())
            adapter = FriendForCaseAdapter(requireContext(),it.map{item -> FriendForCaseAdapter.SelectableUser(item)}.toMutableList()){
            }
            list_friend.adapter = adapter
        }



        check_public.setOnClickListener {
            if(check_public.isChecked){
                sex_limit.visibility = View.VISIBLE;
            }else{
                sex_limit.visibility = View.GONE;
            }
        }

        root.findViewById<Button>(R.id.btn_finish).setOnClickListener {
            viewModel.setInvite(adapter.getData().filter{ it.selected }.map{it.id}.joinToString(","))

            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            if(check_public.isChecked){
                val limit:String = when(RadioGroup_sex!!.checkedRadioButtonId){
                    R.id.RadioButton_M ->   "M"
                    R.id.RadioButton_F ->   "F"
                    R.id.RadioButton_A ->   "A"
                    else -> "A"
                }
                viewModel.setLimit(limit);

                if(viewModel.invite.value==""){
                    viewModel.setPublic(formatter.format(Calendar.getInstance().time))
                    (activity as EdAddCaseActivity?)!!.createCase()
                }else (activity as EdAddCaseActivity?)!!.navController.navigate(R.id.action_addCaseStep3Fragment_to_addCaseStep4Fragment)
            }else{
                viewModel.setLimit("A")
                if(viewModel.invite.value=="") {
                    viewModel.setPublic(formatter.format(Calendar.getInstance().time))
                }
                 (activity as EdAddCaseActivity?)!!.createCase()
            }
        }
        return root
    }

    private fun getFriends(callback:(Array<User>)->Unit) {
        val stringRequest: StringRequest = object : StringRequest(Method.POST,  Global.url + "setFriend.php", Response.Listener { response: String? ->
            Log.d("connect", "select Response: $response")
            try {
                callback(Gson().fromJson(response, Array<User>::class.java))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error: VolleyError? -> Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show() }) {
            override fun getParams(): MutableMap<String?, String?> {
                val data: MutableMap<String?, String?> = HashMap()
                data["type"] = "select-elder"
                data["uid"] = uid.toString()
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

}