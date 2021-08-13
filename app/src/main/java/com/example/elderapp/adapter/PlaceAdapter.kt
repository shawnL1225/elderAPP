package com.example.elderapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elderapp.R

class PlaceAdapter(private val data: MutableList<Place>) : RecyclerView.Adapter<PlaceAdapter.ViewHolder?>() {
    private var mClickListener: ItemClickListener? = null

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
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
        private var title = itemView.findViewById<TextView>(R.id.tv_name)
        private var desc = itemView.findViewById<TextView>(R.id.tv_phone)
        private var delete = itemView.findViewById<ImageView>(R.id.img_delete)
        private var icon = itemView.findViewById<ImageView>(R.id.img_headshot)


        fun setData(data: Place) {
            title.text = data.title
            desc.text = data.description
            when(data.iconID){
                1 -> icon.setImageResource(R.drawable.ic_place_home)
                2 -> icon.setImageResource(R.drawable.ic_place_park)
                3 -> icon.setImageResource(R.drawable.ic_place_people)
                4 -> icon.setImageResource(R.drawable.ic_place_hospital)
                5 -> icon.setImageResource(R.drawable.ic_place_coffee)
            }

            delete.setOnClickListener {
                if (mClickListener != null) mClickListener!!.onItemClick(adapterPosition)
            }
        }


    }

    // convenience method for getting data at click position
    fun getTitle(id: Int): String {
        return data[id].title
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