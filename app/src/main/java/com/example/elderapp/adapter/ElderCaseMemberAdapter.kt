package com.example.elderapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.elderapp.Global
import com.example.elderapp.R

class ElderCaseMemberAdapter(private val context: Context,
                             private val data: MutableList<User>,
                             private val case: Case,
                             private var onCancel: ((id:Int) -> Unit)? = null
) : RecyclerView.Adapter<ElderCaseMemberAdapter.ViewHolder?>() {

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_elder_case_member, parent, false)
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
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var img_headshot = itemView.findViewById<ImageView>(R.id.img_headshot)
        private var txt_name = itemView.findViewById<TextView>(R.id.txt_name)
        private var txt_status = itemView.findViewById<TextView>(R.id.txt_status)
        private var btn_cancel = itemView.findViewById<Button>(R.id.btn_cancel)


        fun setData(data: User) {
            Global.headUp(context, img_headshot, data.headshot)
            txt_name.text = data.name

            when {
                case.receiver == null -> {
                    txt_status.visibility = View.VISIBLE
                    txt_status.text = "等待中"
                    txt_status.setTextColor(context.resources.getColor(R.color.toolBar))
                    btn_cancel.visibility = View.GONE
                }
                case.receiver!!.id == data.id -> {
                    txt_status.visibility = View.VISIBLE
                    txt_status.text = "已接受"
                    txt_status.setTextColor(context.resources.getColor(R.color.successful_green))
                    btn_cancel.visibility = View.VISIBLE
                }
                else -> {
                    txt_status.visibility = View.GONE
                    btn_cancel.visibility = View.GONE
                }
            }

            btn_cancel.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("確定要婉拒此志工嗎`?")
                        .setPositiveButton("是",
                                DialogInterface.OnClickListener { dialog, id ->
                                    onCancel?.let { it1 -> it1(case.id) }
                                })
                        .setNegativeButton("否",
                                DialogInterface.OnClickListener { dialog, id ->
                                })
                builder.create()
                builder.show()
            }


        }


    }

}