package com.example.elderapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elderapp.R

class DiscussTitleAdapter(private val context: Context, private val data:MutableList<DiscussTitle>, private val uid:Int):RecyclerView.Adapter<DiscussTitleAdapter.ViewHolder>() {
    private var mClickListener: ItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_discuss_title, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = data[position].title
        holder.itemView.setOnClickListener {
            mClickListener!!.onItemClick(position)
        }
        if(data[position].uid != uid){
            holder.btnDelete.visibility = View.INVISIBLE
        }
        holder.btnDelete.setOnClickListener {
            mClickListener!!.onDeleteClick(position)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.textView)!!
        val btnDelete = itemView.findViewById<ImageView>(R.id.btn_delete)!!

    }
    fun getDiscuss(pos:Int): DiscussTitle{
        return data[pos]
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(position: Int)
        fun onDeleteClick(position: Int)
    }
}