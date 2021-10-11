package com.example.elderapp

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class Global : Application() {
    companion object {
//        var url: String? = "https://www2.cs.ccu.edu.tw/~lwx109u/elderApp/"
        var url: String? = "http://140.123.105.18:3000/elderAPP/elderSql/"
        var urlData: String? = "http://140.123.105.18:3000/elderAPP/imgData/"

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
                Log.d("request", "Response: $response")
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

        fun headUp(context:Context, img: ImageView, headshot:String){
                val idx = when(headshot){
                    "default_m" -> 0
                    "default_f" -> 1
                    "default_n" -> 2
                    else -> 3
                }
                if(idx==3){
                    var url ="${Global.urlData}headshot/${headshot}.jpg"
                    Glide.with(context)
                            .load(url)
                            .circleCrop()
                            .into(img)
                    Log.d("Loading", "url$url")
                }else {
                    Glide.with(context)
                            .load(arrayOf(R.drawable.male_nobg, R.drawable.female_nobg, R.drawable.nonsex)[idx])
                            .circleCrop()
                            .into(img)

                    Log.d("Loading", "Load as draw")
                }
        }

        fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
            var width = image.width
            var height = image.height
            val bitmapRatio = width.toFloat() / height.toFloat()
            if (bitmapRatio > 1) {
                width = maxSize
                height = (width / bitmapRatio).toInt()
            } else {
                height = maxSize
                width = (height * bitmapRatio).toInt()
            }
            return Bitmap.createScaledBitmap(image, width, height, true)
        }


    }


}