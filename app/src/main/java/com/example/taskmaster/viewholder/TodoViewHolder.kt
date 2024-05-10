package com.example.taskmaster.viewholder

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaster.R

class TodoViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val cpTodo: CheckBox = view.findViewById(R.id.cpTodo)
    val ivDelete: ImageView = view.findViewById(R.id.ivDelete)


}