package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmaster.adapter.TodoAdapter
import com.example.taskmaster.database.TodoDatabase
import com.example.taskmaster.repositories.TodoRepository
import com.example.taskmaster.viewmodel.MainActivityData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CompletedTodos : AppCompatActivity() {

    private lateinit var BtnMainBack: View
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var viewModel: MainActivityData
    private lateinit var recyclerView: RecyclerView
    private lateinit var repository: TodoRepository
    private lateinit var countCompletedTodos: TextView
    private lateinit var CompletedMsg: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_completed_todos)

        repository = TodoRepository(TodoDatabase.getInstance(this))
        //initialize the view model
        viewModel = ViewModelProvider(this)[MainActivityData::class.java]

        recyclerView = findViewById(R.id.rvCompletedTodoList)
        countCompletedTodos = findViewById(R.id.countCompleted)
        BtnMainBack = findViewById(R.id.btnMain)
        CompletedMsg = findViewById(R.id.msgCompleted)


        //hide the count text view
        countCompletedTodos.visibility = View.GONE

        //get all the completed todos
        CoroutineScope(Dispatchers.IO).launch {
            val data = repository.getCompletedTodos()
            withContext(Dispatchers.Main){
                viewModel.setCompleted(data)
            }
        }

        //get the total number of completed todos
        //getCompletedTodos() have to run on a background thread as they are suspend functions
        CoroutineScope(Dispatchers.IO).launch {
            val countCompleted = repository.getCompletedTodos().size
            withContext(Dispatchers.Main){
                viewModel.setCompletedCount(countCompleted)
            }
        }

        //observe the data in the view model
        viewModel.completed.observe(this){
            if(it.isEmpty()){
                CompletedMsg.visibility = View.VISIBLE
            }else{
                CompletedMsg.visibility = View.GONE
            }
            todoAdapter = TodoAdapter(it, repository, viewModel)
            recyclerView.adapter = todoAdapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }

        //display the totol number of completed todos
        viewModel.completedCount.observe(this){
            if(it == 0){
                countCompletedTodos.visibility = View.GONE
            }else{
                countCompletedTodos.visibility = View.VISIBLE
                //set the text of the count text view
                countCompletedTodos.text = "Total Completed Todos: $it"
            }

        }


        BtnMainBack.setOnClickListener {
            navigateToMainMenu(View(this))
        }
    }

    fun navigateToMainMenu(v: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}