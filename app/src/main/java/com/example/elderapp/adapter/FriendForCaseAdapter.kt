package com.example.elderapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.elderapp.Global
import com.example.elderapp.R

class FriendForCaseAdapter(private val context: Context, private var data: MutableList<SelectableUser>, private val onClick: (p:Place) -> Unit) : RecyclerView.Adapter<FriendForCaseAdapter.ViewHolder?>() {

    class SelectableUser(user:User): User(user.id,user.name,user.phone) {
        var selected = false

        fun head(headshot:String) :SelectableUser {
            this.headshot = headshot
            return this
        }
    }


    private var mClickListener: ItemClickListener? = null

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend_for_case, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
    fun getData(): MutableList<SelectableUser> {
        return data;
    }

    // ViewHolder
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var tvName = itemView.findViewById<TextView>(R.id.tv_name)
        private var tvPhone = itemView.findViewById<TextView>(R.id.tv_phone)
        private var back = itemView.findViewById<ConstraintLayout>(R.id.back)
        private var imgHeadShot = itemView.findViewById<ImageView>(R.id.img_headshot)

        fun setData(data: SelectableUser) {
            tvName.text = data.name
            tvPhone.text = data.phone
            Global.headUp(context, imgHeadShot, data.headshot)
            itemView.setOnClickListener {
                data.selected = !data.selected
                back.backgroundTintList = if(data.selected) context.resources.getColorStateList(R.color.blue_light) else null
            }
        }
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(position: Int)
    }
}