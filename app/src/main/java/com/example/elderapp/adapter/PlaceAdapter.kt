package com.example.elderapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elderapp.R

class PlaceAdapter     // data is passed into the constructor
(private val context: Context?, private val data: MutableList<Place?>?) : RecyclerView.Adapter<PlaceAdapter.ViewHolder?>() {
    private var mClickListener: ItemClickListener? = null

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each row
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder.setData(data.get(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    // ViewHolder
    inner class ViewHolder internal constructor(itemView: View?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var title: TextView?
        var desc: TextView?
        var delete: ImageView?
        override fun onClick(view: View?) {
            if (mClickListener != null) mClickListener.onItemClick(view, adapterPosition)
            //            Toast.makeText(mContext, title.getText().toString()+"hi", Toast.LENGTH_SHORT).show();
        }

        fun setData(data: Place?) {
            title.setText(data.placeTitle)
            desc.setText(data.placeDesc)
        }

        init {
            title = itemView.findViewById(R.id.tv_placeTitle)
            desc = itemView.findViewById(R.id.tv_placeDesc)
            delete = itemView.findViewById(R.id.img_delete)
            delete.setOnClickListener(this)
        }
    }

    // convenience method for getting data at click position
    fun getTitle(id: Int): String? {
        return data.get(id).placeTitle
    }

    fun getId(id: Int): Int {
        return data.get(id).id
    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        open fun onItemClick(view: View?, position: Int)
    }
}