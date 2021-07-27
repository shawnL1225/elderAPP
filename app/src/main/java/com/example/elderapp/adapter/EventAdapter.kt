package com.example.elderapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.elderapp.Global
import com.example.elderapp.R

class EventAdapter(private val context: Context, private val data: MutableList<Event>, private val uid: Int) : RecyclerView.Adapter<EventAdapter.ViewHolder?>() {
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
        private var tvDelete = itemView.findViewById<TextView>(R.id.tv_delete)
        private var tvStatus = itemView.findViewById<TextView>(R.id.tv_status)
        private var tvOwn = itemView.findViewById<TextView>(R.id.tv_own)
        @SuppressLint("SetTextI18n")
        fun setData(data: Event) {
            tvTitle.text = data.title
            tvHolder.text = data.holder
            tvCount.text = data.attendee.size.toString()+" 人已參與"

            if(data.attendee.contains(uid)){
                tvCheck.visibility = View.VISIBLE
            }
            var imgUrl ="${Global.url}event_img/${data.id}.jpg"
            Glide.with(context)
                    .load(imgUrl)
                    .centerCrop()
                    .into(imgEvent)

            itemView.setOnClickListener {
                mClickListener!!.onItemClick(adapterPosition)
            }
            if(data.status == -1){
                tvTitle.alpha = 0.5f
                tvHolder.alpha = 0.5f
                tvCount.alpha = 0.5f
                imgEvent.alpha = 0.5f
                itemView.setBackgroundResource(R.drawable.style_recycler_item_delete)
                tvStatus.visibility = View.VISIBLE
                return
            }
            if(data.holderUid == uid){
                tvOwn.visibility = View.VISIBLE
                tvDelete.visibility = View.VISIBLE
                tvDelete.setOnClickListener{
                    mClickListener!!.onDeleteClick(adapterPosition)
                }
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
        fun onDeleteClick(position: Int)
    }
}