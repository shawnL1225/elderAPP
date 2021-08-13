package com.example.elderapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.elderapp.Global
import com.example.elderapp.R
import com.google.gson.Gson
import java.text.SimpleDateFormat

class VolunteerCaseAdapter(val context: Context, private val data: MutableList<Case>) : RecyclerView.Adapter<VolunteerCaseAdapter.ViewHolder?>() {
    private var onClick: ((Case) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_volunteer_case, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var txt_name = itemView.findViewById<TextView>(R.id.txt_name)
        private var txt_phone = itemView.findViewById<TextView>(R.id.txt_phone)
        private var txt_place = itemView.findViewById<TextView>(R.id.txt_place)
        private var txt_status = itemView.findViewById<TextView>(R.id.txt_status)
        private var txt_date = itemView.findViewById<TextView>(R.id.txt_date)
        private var img_headshot = itemView.findViewById<ImageView>(R.id.img_headshot)



        fun setData(data: Case) {
            txt_name.text = data.submitter.name
            txt_phone.text = data.submitter.phone
            txt_place.text = data.place.title


            val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val formatter = SimpleDateFormat("MM-dd HH:mm")
            val date = formatter.format(parser.parse(data.date))

            txt_date.text = date

            Global.headUp(context,img_headshot,data.submitter.headshot)

            Log.d("Sub", Gson().toJson(data))
            val uid = context.getSharedPreferences("loginUser", AppCompatActivity.MODE_PRIVATE).getString("uid", "")
            if(uid?.toInt() ?:0 === data.receiver?.id ?: -1){
                txt_status.text = "已接受"
                txt_status.backgroundTintList = context.resources.getColorStateList(R.color.successful_green)
            }

            itemView.setOnClickListener {
                if (onClick != null) onClick!!(data)
            }
        }


    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: (Case) -> Unit):VolunteerCaseAdapter {
        onClick = itemClickListener
        return this
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(position: Int)
    }
}