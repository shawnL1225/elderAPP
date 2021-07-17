package com.example.elderapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elderapp.R

class FriendAdapter(private val data: MutableList<User>) : RecyclerView.Adapter<FriendAdapter.ViewHolder?>() {
    private var mClickListener: ItemClickListener? = null

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
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
        private var tvName = itemView.findViewById<TextView>(R.id.tv_name)
        private var tvPhone = itemView.findViewById<TextView>(R.id.tv_phone)
        private var imgDelete = itemView.findViewById<ImageView>(R.id.img_delete)
        private var imgHeadShot = itemView.findViewById<ImageView>(R.id.img_headshot)


        fun setData(data: User) {
            tvName.text = data.name
            tvPhone.text = data.phone
            //set headShot

            imgDelete.setOnClickListener {
                if (mClickListener != null) mClickListener!!.onItemClick(adapterPosition)
            }
        }


    }

    // convenience method for getting data at click position
    fun getTitle(id: Int): String {
        return data[id].name
    }

    fun getId(id: Int): Int {
        return data[id].id
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