package com.example.taskmaster.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
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
private const val TAG  = "MainActivity"
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
        //check if the task is completed
        if(task.isCompleted == 1){
            holder.tdCheckbox.isChecked = true
        }

        //set the on click listener for the checkbox
        holder.tdCheckbox.setOnClickListener{
            //check if the checkbox is check  ed
            if(holder.tdCheckbox.isChecked){
                //Log.i(TAG, "Button clicked :${task.title} ")
                //io is because we are doing database operation
                CoroutineScope(Dispatchers.IO).launch {
                    //mark the task as completed query the database
                    task.id?.let { it1 -> repository.markAsCompleted(it1) }
                    //get the completed todos count
                    val countCompleted = repository.getCompletedTodos().size
                    //get the completed todos
                    val completed = repository.getCompletedTodos()
                    //change the context to main because we are updating the ui
                    withContext(Dispatchers.Main) {
                        //update the view model
                        viewModel.setCompleted(completed)
                        viewModel.setCompletedCount(countCompleted)
                    }
                }
                //show a toast message
                Toast.makeText(context, "Task Marked as Completed", Toast.LENGTH_SHORT).show()
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    task.id?.let { it1 -> repository.markAsInComplete(it1) }
                    val completed = repository.getCompletedTodos()
                    val countCompleted = repository.getCompletedTodos().size
                    withContext(Dispatchers.Main) {
                        viewModel.setCompleted(completed)
                        viewModel.setCompletedCount(countCompleted)
                    }
                }
                Toast.makeText(context, "Task Mark as Incomplete", Toast.LENGTH_SHORT).show()
            }
        }



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
                    val count = repository.getTodosCount()
                    val completed = repository.getCompletedTodos()
                    val countCompleted = repository.getCompletedTodos().size
                    //change the context to main because we are updating the ui
                    withContext(Dispatchers.Main) {
                        viewModel.setData(todoItems)
                        viewModel.setCount(count)
                        viewModel.setCompleted(completed)
                        viewModel.setCompletedCount(countCompleted)
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