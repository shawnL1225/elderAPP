package com.example.elderapp.elder

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elderapp.Global
import com.example.elderapp.R
import com.example.elderapp.adapter.TodoAdapter
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.gson.Gson
import java.util.ArrayList


class TodoFragment : Fragment(), TodoAdapter.ItemClickListener {
    lateinit var adapter:TodoAdapter
    lateinit var recyclerView:RecyclerView
    var todoList: MutableList<String> = ArrayList()
    lateinit var uid :String
    private var itemNum:Int? = null
    lateinit var etContent:EditText
    lateinit var content:String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_todo, container, false)

        uid = requireActivity().getSharedPreferences("loginUser", AppCompatActivity.MODE_PRIVATE).getString("uid", "")!!
        recyclerView = root.findViewById<RecyclerView>(R.id.recycler_todo)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        getTodoList()
        val btnAdd = root.findViewById<ExtendedFloatingActionButton>(R.id.btn_add)
        btnAdd.setOnClickListener {
            addTodoList()
        }
        return root
    }

    private fun getTodoList() {


        val gson = Gson()
        val json = requireActivity().getSharedPreferences("todoList", AppCompatActivity.MODE_PRIVATE)
                .getString("items", "")
        if (json!!.isEmpty()) {

        } else {
            todoList = gson.fromJson(json, MutableList::class.java) as MutableList<String>;
        }

        adapter = TodoAdapter(todoList)
        adapter.setClickListener(this)
        recyclerView.adapter = adapter

    }

    private fun addTodoList(){
        val builder = AlertDialog.Builder(requireContext())
        val dialog = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_todo, null)
        etContent = dialog.findViewById(R.id.et_content)

        builder.setTitle("新增待辦事項").setView(dialog)
                .setPositiveButton("確認") { dialogInterface: DialogInterface, i: Int ->
                    content = etContent.text.toString().trim()
                    todoList.add(content)
                    val gson = Gson()
                    val json = gson.toJson(todoList)
                    requireActivity().getSharedPreferences("todoList", AppCompatActivity.MODE_PRIVATE).edit()
                            .putString("items", json)
                            .apply()
                    getTodoList()
                }
                .setNegativeButton("取消") { _, _-> }
                .show()



    }

    override fun onDeleteClick(position: Int) {
        todoList.removeAt(position)
        updateListToSp()
        getTodoList()
        Global.putSnackBar(recyclerView, "成功刪除")
    }

    override fun onEditClick(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        val dialog = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_todo, null)
        etContent = dialog.findViewById(R.id.et_content)
        etContent.setText(todoList[position])

        builder.setTitle("修改待辦事項").setView(dialog)
                .setPositiveButton("確認") { dialogInterface: DialogInterface, i: Int ->
                    content = etContent.text.toString().trim()
                    todoList[position] = content
                    updateListToSp()
                    getTodoList()
                }
                .setNegativeButton("取消") { _, _-> }
                .show()
    }
    private fun updateListToSp(){
        val gson = Gson()
        val json = gson.toJson(todoList)
        requireActivity().getSharedPreferences("todoList", AppCompatActivity.MODE_PRIVATE).edit()
                .putString("items", json)
                .apply()
    }
}