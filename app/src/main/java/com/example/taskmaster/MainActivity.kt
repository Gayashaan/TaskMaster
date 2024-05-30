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
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    //create a lateinit variable
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var viewModel: MainActivityData
    private lateinit var countTodos: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var repository: TodoRepository
    private lateinit var addItemBtn: FloatingActionButton
    private lateinit var completedBtn: FloatingActionButton
    private lateinit var msgHeader: TextView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        //initialize the add item button
        addItemBtn = findViewById(R.id.btnAdd)
        // val addItemBtn = findViewById <FloatingActionButton>(R.id.btnAdd)
        // val addItemBtn: FloatingActionButton = findViewById(R.id.btnAdd)

        //initialize the completed button
        completedBtn = findViewById(R.id.btnCompleted)
        //initialize the count text view
        countTodos = findViewById<Button>(R.id.count)
        //initialize the message header
        msgHeader = findViewById(R.id.msg)
        //initialize the recycler view
        recyclerView = findViewById(R.id.rvTodoList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        //initialize the repository
        repository = TodoRepository(TodoDatabase.getInstance(this))
        //initialize the view model
        viewModel = ViewModelProvider(this)[MainActivityData::class.java]

        //hide the message header and the count text view
        msgHeader.visibility = View.GONE
        countTodos.visibility = View.GONE
        completedBtn.visibility = View.GONE

        //observe the data in the view model
        viewModel.data.observe(this){
            if(it.isEmpty()) {
                msgHeader.visibility = View.VISIBLE
            }else{
                msgHeader.visibility = View.GONE
            }
            todoAdapter = TodoAdapter(it, repository, viewModel)
            recyclerView.adapter = todoAdapter
        }

        //display the totol number of todos
        viewModel.count.observe(this){
            if(it == 0){
                countTodos.visibility = View.GONE
            }else{
                countTodos.visibility = View.VISIBLE
            }
            //set the text of the count text view
            countTodos.text = "Total Todos: $it"
        }

        //display the totol number of todos
        viewModel.completedCount.observe(this){
            if(it == 0){
                completedBtn.visibility = View.GONE
            }else{
                completedBtn.visibility = View.VISIBLE
            }
        }

        //get the total number of todos
        //getTodosCount() have to run on a background thread as they are suspend functions
        CoroutineScope(Dispatchers.IO).launch {
            val count = repository.getTodosCount()
            withContext(Dispatchers.Main){
                viewModel.setCount(count)
            }
        }

        //get all the todos
        CoroutineScope(Dispatchers.IO).launch {
            val data = repository.getAllTodos()
            withContext(Dispatchers.Main){
                viewModel.setData(data)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val countCompleted = repository.getCompletedTodos().size
            withContext(Dispatchers.Main){
                viewModel.setCompletedCount(countCompleted)
            }
        }

        //set the on click listener for the add item button
        addItemBtn.setOnClickListener{
            navigateToAddTodoActivity(it)
            //displayAlert(repository)
        }

        completedBtn.setOnClickListener{
            navigateToCompletedTodos(it)
            //displayAlert(repository)
        }

        //hide the add item button when the user scrolls down
        recyclerView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY) {
                addItemBtn.hide()
                completedBtn.hide()
            } else {
                addItemBtn.show()
                completedBtn.show()
            }
        }


    }

    //navigate to the add todo activity
    fun navigateToAddTodoActivity(v: View){
        val intent = Intent(this, AddTodoActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun navigateToCompletedTodos(v: View){
        val intent = Intent(this, CompletedTodos::class.java)
        startActivity(intent)
        finish()
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