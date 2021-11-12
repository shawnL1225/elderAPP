package com.example.elderapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elderapp.R
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class ElderCaseAdapter(private val context: Context, private val data: MutableList<Case>,
                       private var onCancel: ((id:Int) -> Unit)? = null,  private var onCancelReceiver: ((id:Int) -> Unit)? = null) : RecyclerView.Adapter<ElderCaseAdapter.ViewHolder?>() {

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_elder_case, parent, false)
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
        private var txt_date = itemView.findViewById<TextView>(R.id.txt_date)
        private var img_place = itemView.findViewById<ImageView>(R.id.img_place)
        private var txt_place = itemView.findViewById<TextView>(R.id.txt_place)
        private var txt_status = itemView.findViewById<TextView>(R.id.txt_status)
        private var list_candidate = itemView.findViewById<RecyclerView>(R.id.list_candidate)
        private var btn_cancel = itemView.findViewById<Button>(R.id.btn_cancel)
        private var txt_public = itemView.findViewById<TextView>(R.id.txt_public)

        fun setData(data: Case) {
            val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
            txt_date.text = formatter.format(parser.parse(data.date))
            txt_status.text = when (data.receiver) {
                null -> "已邀請"
                else -> "已敲定"
            }

            txt_status.setTextColor(context.resources.getColor(when {
                data.receiver == null -> R.color.toolBar
                else -> R.color.green_dark
            }))

            txt_public.visibility =if (data.public!=null &&  SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.public).before(Calendar.getInstance().time)) {
                View.VISIBLE
            }
            else View.GONE

            img_place.setImageResource(
                    when (data.place.iconID) {
                        1 -> R.drawable.ic_place_home
                        2 -> R.drawable.ic_place_park
                        3 -> R.drawable.ic_place_people
                        4 -> R.drawable.ic_place_hospital
                        5 -> R.drawable.ic_place_coffee
                        else -> R.drawable.ic_place_home
                    })

            txt_place.text = data.place.title

            val list = data.invited.toMutableList()
            if(data.receiver!==null && list.find { it.id == data.receiver!!.id } == null){
                list.add(data.receiver!!)
            }
            Log.d("Test",Gson().toJson(list))
            list_candidate.layoutManager = LinearLayoutManager(context)
            list_candidate.adapter = ElderCaseMemberAdapter(context,list , data,onCancelReceiver)

            btn_cancel.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("確定要取消此行程嗎`?")
                        .setPositiveButton("是",
                                DialogInterface.OnClickListener { dialog, id ->
                                    onCancel?.let { it1 -> it1(data.id) }
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