package com.example.elderapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elderapp.R

class EventAdapter(private val data: MutableList<Event>, private val uid: Int) : RecyclerView.Adapter<EventAdapter.ViewHolder?>() {
    private var mClickListener: ItemClickListener? = null

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    // ViewHolder
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        private var tvLocation = itemView.findViewById<TextView>(R.id.tv_location)
        private var tvDate = itemView.findViewById<TextView>(R.id.tv_date)
        private var tvHolder = itemView.findViewById<TextView>(R.id.tv_holder)
        private var tvCount = itemView.findViewById<TextView>(R.id.tv_count)
        private var tvCheck = itemView.findViewById<TextView>(R.id.tv_check)
        private var imgEvent = itemView.findViewById<ImageView>(R.id.img_event)



        @SuppressLint("SetTextI18n")
        fun setData(data: Event) {
            tvTitle.text = data.title
            tvLocation.text = "地點: "+data.location
            tvDate.text = "時間: "+data.date.replace(' ', '\n')
            tvHolder.text = "發起人:"+data.holder
            tvCount.text = data.attendee.size.toString()+" 人已參與"

            if(data.attendee.contains(uid)){
                tvCheck.visibility = View.VISIBLE
            }




            itemView.setOnClickListener {
                if (mClickListener != null) mClickListener!!.onItemClick(adapterPosition)
            }
        }


    }


    fun getEvent(id: Int): Event {
        return data[id]
    }



    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(position: Int)
    }
}