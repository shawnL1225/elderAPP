package com.example.elderapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elderapp.Global
import com.example.elderapp.R
import java.text.SimpleDateFormat

class CaseAdapter(val context: Context, private val data: MutableList<Case>) : RecyclerView.Adapter<CaseAdapter.ViewHolder?>() {
    private var mClickListener: ItemClickListener? = null

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
        private var txt_date = itemView.findViewById<TextView>(R.id.txt_date)
        private var imgEvent = itemView.findViewById<ImageView>(R.id.img_event)



        fun setData(data: Case) {
            txt_name.text = data.submitter.name
            txt_phone.text = data.submitter.phone
            txt_place.text = data.place.placeTitle
            val format = SimpleDateFormat("MM-dd HH-mm")
            txt_date.text =   format.format(data.date)
            Global.headUp(context,imgEvent,data.submitter.headshot)

//            itemView.setOnClickListener {
//                if (mClickListener != null) mClickListener!!.onItemClick(adapterPosition)
//            }
        }


    }


//    fun getName(id: Int): String {
//        return data[id].name
//    }
//
//    fun getId(id: Int): Int {
//        return data[id].id
//    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(position: Int)
    }
}