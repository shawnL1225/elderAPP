package com.example.elderapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elderapp.R

class DiscussCommentAdapter(private val context: Context, private val data:MutableList<DiscussComment>): RecyclerView.Adapter<DiscussCommentAdapter.ViewHolder>() {
    private var mClickListener: ItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_discuss_comment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.comment.text = data[position].comment
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val comment = itemView.findViewById<TextView>(R.id.textView)!!
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(position: Int)
    }
}