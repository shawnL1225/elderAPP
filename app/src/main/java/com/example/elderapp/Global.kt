package com.example.elderapp

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class Global : Application() {
    companion object {
        var url: String? = "https://www2.cs.ccu.edu.tw/~lwx109u/elderApp/"
        //    public static String url = "http://140.123.105.178/~tcus/elderApp/";

        fun putSnackBar(view: View, s: String) {
            val snackBar = Snackbar.make(view, s, Snackbar.LENGTH_SHORT)
            snackBar.view.setBackgroundResource(R.color.orange)
            snackBar.show()
        }

        fun putSnackBarR(view: View, s: String) {
            val snackBar = Snackbar.make(view, s, Snackbar.LENGTH_SHORT)
            snackBar.view.setBackgroundResource(R.color.dangerous_red)
            snackBar.show()
        }

        fun profile(context: Context, uid: String, callback: (RawUser) -> Unit) {
            Log.d("profile", "Executed")

            val stringRequest: StringRequest = object : StringRequest(Method.POST, url + "setProfile.php", Response.Listener { response: String ->
                Log.d("connect", "Response: $response")
                try {
                    Log.d("Profile", response)
                    val gson = Gson()
                    callback(gson.fromJson(response, RawUser::class.java))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }, Response.ErrorListener { error: VolleyError -> Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show() }) {
                override fun getParams(): MutableMap<String?, String?> {
                    val data: MutableMap<String?, String?> = HashMap()
                    data["type"] = "getData"
                    data["uid"] = uid
                    return data
                }
            }
            val requestQueue = Volley.newRequestQueue(context)
            requestQueue.add(stringRequest)
        }
    }


}