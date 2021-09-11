package com.example.elderapp.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import android.graphics.drawable.Drawable
import android.graphics.PorterDuff




class DiscussCommentAdapter(private val context: Context, private val data:MutableList<DiscussComment>, private val uid:Int): RecyclerView.Adapter<DiscussCommentAdapter.ViewHolder>() {
    private var mClickListener: ItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.example.elderapp.R.layout.item_discuss_comment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.comment.text = data[position].comment
        holder.btnEdit.setOnClickListener {
            mClickListener!!.onEditClick(position)
        }
        holder.btnDelete.setOnClickListener {
            mClickListener!!.onDeleteClick(position)
        }
        if(data[position].uid == uid){
            holder.btnEdit.visibility = View.VISIBLE
            holder.btnDelete.visibility = View.VISIBLE
            val drawable: Drawable = context.resources.getDrawable(com.example.elderapp.R.drawable.nonsex).mutate()
            drawable.setColorFilter(
                context.resources.getColor(com.example.elderapp.R.color.brown),
                PorterDuff.Mode.SRC_ATOP
            )
            holder.imgHead.setImageDrawable(drawable)


        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val comment = itemView.findViewById<TextView>(com.example.elderapp.R.id.textView)!!
        val btnEdit = itemView.findViewById<ImageView>(com.example.elderapp.R.id.btn_edit)!!
        val btnDelete = itemView.findViewById<ImageView>(com.example.elderapp.R.id.btn_delete)!!
        val imgHead = itemView.findViewById<ImageView>(com.example.elderapp.R.id.img_head)!!
    }

    fun getComment(pos:Int):DiscussComment{
        return data[pos]
    }
    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onEditClick(position: Int)
        fun onDeleteClick(position: Int)
    }
}