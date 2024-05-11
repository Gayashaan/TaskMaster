package com.example.taskmaster.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaster.R
import com.example.taskmaster.UpdateTodoActivity
import com.example.taskmaster.entities.Todo
import com.example.taskmaster.repositories.TodoRepository
import com.example.taskmaster.viewholder.TodoViewHolder
import com.example.taskmaster.viewmodel.MainActivityData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoAdapter(items: List<Todo>, repo: TodoRepository,
                  viewModel: MainActivityData): RecyclerView.Adapter<TodoViewHolder>() {

    private var context: Context? = null
    private var todoItems = items
    private var repository = repo
    val viewModel = viewModel


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item_todo, parent, false)
        context = parent.context
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todoItems.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {

        val task = todoItems[position]

        //set the data to the view this is where we bind the data to the view
        holder.title.text = task.title
        holder.description.text = task.description


        holder.card.setOnClickListener() {
            val context = holder.itemView.context
            val intent = Intent(context, UpdateTodoActivity::class.java)
            intent.putExtra("TODO_ID", task.id)
            context.startActivity(intent)
        }


        holder.ivDelete.setOnClickListener() {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete Todo")
            builder.setMessage("Are you sure you want to delete this todo?")
            builder.setPositiveButton("Yes") { _, _ ->
                //io is because we are doing database operation
                CoroutineScope(Dispatchers.IO).launch {
                    repository.delete(todoItems[position])
                    todoItems = repository.getAllTodos()
                    //change the context to main because we are updating the ui
                    withContext(Dispatchers.Main) {
                        viewModel.setData(todoItems)
                    }
                }
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }
}