package com.example.taskmaster.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaster.R
import com.example.taskmaster.viewholder.TodoViewHolder
import com.google.android.material.transition.Hold

class TodoAdapter: RecyclerView.Adapter<TodoViewHolder>() {
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item_todo, parent, false)
        context = parent.context
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.cpTodo.text = "Task 1"
        holder.ivDelete.setOnClickListener(){
            Toast.makeText(context, "Button is clicked $position", Toast.LENGTH_SHORT).show()
        }
    }
}