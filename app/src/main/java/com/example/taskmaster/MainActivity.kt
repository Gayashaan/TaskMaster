package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.service.controls.actions.FloatAction
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.TextView
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

    //create a lateinit variable
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var viewModel: MainActivityData
    private lateinit var count: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var repository: TodoRepository
    private lateinit var addItemBtn: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //initialize the recycler view
        recyclerView = findViewById(R.id.rvTodoList)
        //initialize the repository
        repository = TodoRepository(TodoDatabase.getInstance(this))
        //initialize the view model
        viewModel = ViewModelProvider(this)[MainActivityData::class.java]

        //observe the data in the view model
        viewModel.data.observe(this){
            todoAdapter = TodoAdapter(it, repository, viewModel)
            recyclerView.adapter = todoAdapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }

        //display the totol number of todos
        viewModel.count.observe(this){
            //initialize the count text view
            count = findViewById<Button>(R.id.count)
            //set the text of the count text view
            count.text = "Total Todos: $it"
        }

        //get the total number of todos
        //getTodosCount() have to run on a background thread as they are suspend functions
        CoroutineScope(Dispatchers.IO).launch {
            val count = repository.getTodosCount()
            runOnUiThread(){
                viewModel.setCount(count)
            }
        }

        //get all the todos
        CoroutineScope(Dispatchers.IO).launch {
            val data = repository.getAllTodos()
            runOnUiThread(){
                viewModel.setData(data)
            }
        }

        //initialize the add item button
        addItemBtn = findViewById(R.id.btnAdd)

        //set the on click listener for the add item button
        addItemBtn.setOnClickListener{
            navigateToAddTodoActivity(it)
            //displayAlert(repository)
        }

        //hide the add item button when the user scrolls down
        recyclerView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                addItemBtn.hide()
            } else {
                addItemBtn.show()
            }
        }


    }

    //navigate to the add todo activity
    fun navigateToAddTodoActivity(v: View){
        val intent = Intent(this, AddTodoActivity::class.java)
        startActivity(intent)
    }

//    private fun displayAlert(repository: TodoRepository){
//        val builder = AlertDialog.Builder(this)
//
//        //todo add the string to resources R.string.add_task STRING
//        builder.setTitle("Add Task")
//        builder.setMessage("Enter the task to be added")
//
//        val input = android.widget.EditText(this)
//        input.inputType = InputType.TYPE_CLASS_TEXT
//        builder.setView(input)
//
//        builder.setPositiveButton("ok"){
//            dialog, which ->
//            val item = input.text.toString()
//            CoroutineScope(Dispatchers.IO).launch {
//                repository.insert(Todo(item))
//                val data = repository.getAllTodos()
//                runOnUiThread() {
//                    viewModel.setData(data)
//                }
//            }
//        }
//
//        builder.setNegativeButton("cancel"){
//            dialog, which ->
//            dialog.cancel()
//        }
//
//        val alertDialog = builder.create()
//        alertDialog.show()
//
//    }
}