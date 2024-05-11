package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.service.controls.actions.FloatAction
import android.text.InputType
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaster.adapter.TodoAdapter
import com.example.taskmaster.entities.Todo
import com.example.taskmaster.database.TodoDatabase
import com.example.taskmaster.repositories.TodoRepository
import com.example.taskmaster.viewmodel.MainActivityData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var todoAdapter: TodoAdapter
    private lateinit var viewModel: MainActivityData
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.rvTodoList)

        val repository = TodoRepository(TodoDatabase.getInstance(this))

        viewModel = ViewModelProvider(this)[MainActivityData::class.java]


        viewModel.data.observe(this){
            todoAdapter = TodoAdapter(it, repository, viewModel)
            recyclerView.adapter = todoAdapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val data = repository.getAllTodos()

            runOnUiThread(){
                viewModel.setData(data)
            }
        }

        val addItemBtn: FloatingActionButton = findViewById(R.id.btnAdd)

        addItemBtn.setOnClickListener{
            navigateToAddTodoActivity(it)
            //displayAlert(repository)
        }

    }

    fun navigateToAddTodoActivity(v: View){
        val intent = Intent(this, AddTodoActivity::class.java)
        startActivity(intent)
    }

    private fun displayAlert(repository: TodoRepository){
        val builder = AlertDialog.Builder(this)

        //todo add the string to resources R.string.add_task STRING
        builder.setTitle("Add Task")
        builder.setMessage("Enter the task to be added")

        val input = android.widget.EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("ok"){
            dialog, which ->
            val item = input.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                repository.insert(Todo(item))
                val data = repository.getAllTodos()
                runOnUiThread() {
                    viewModel.setData(data)
                }
            }
        }

        builder.setNegativeButton("cancel"){
            dialog, which ->
            dialog.cancel()
        }

        val alertDialog = builder.create()
        alertDialog.show()

    }
}