package com.example.elderapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elderapp.R

class TodoAdapter(private val data: MutableList<String>) : RecyclerView.Adapter<TodoAdapter.ViewHolder?>() {
    private var mClickListener: ItemClickListener? = null

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
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
        private var content = itemView.findViewById<TextView>(R.id.tv_content)
        private var delete = itemView.findViewById<ImageView>(R.id.img_delete)
        private var edit = itemView.findViewById<ImageView>(R.id.img_edit)


        fun setData(data: String) {
            content.text = data

            delete.setOnClickListener {
                if (mClickListener != null) mClickListener!!.onDeleteClick(adapterPosition)
            }
            edit.setOnClickListener {
                if (mClickListener != null) mClickListener!!.onEditClick(adapterPosition)
            }
        }


    }


    fun getContent(id: Int): String {
        return data[id]
    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onDeleteClick(position: Int)
        fun onEditClick(position: Int)
    }
}