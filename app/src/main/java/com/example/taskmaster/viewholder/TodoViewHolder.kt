package com.example.taskmaster.viewholder

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaster.R

class TodoViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val title: TextView = view.findViewById(R.id.td_title)
    val description: TextView = view.findViewById(R.id.td_description)
    val ivDelete: ImageView = view.findViewById(R.id.ivDelete)
    val card: View = view.findViewById(R.id.listCardView)


}