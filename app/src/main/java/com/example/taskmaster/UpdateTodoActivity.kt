package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.taskmaster.database.TodoDatabase
import com.example.taskmaster.entities.Todo
import com.example.taskmaster.repositories.TodoRepository
import com.example.taskmaster.viewmodel.MainActivityData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateTodoActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityData
    private lateinit var title: EditText
    private lateinit var description: EditText
    private lateinit var updateBtn: Button
    private lateinit var backBtn: Button
    private lateinit var repository: TodoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_todo)
        // get task ID from intent extras
        val taskId = intent.getIntExtra("TODO_ID", -1)

        repository = TodoRepository(TodoDatabase.getInstance(this))
        viewModel = ViewModelProvider(this)[MainActivityData::class.java]

        title = findViewById(R.id.update_title)
        description = findViewById(R.id.update_description)
        updateBtn = findViewById(R.id.updateBtn)
        backBtn = findViewById(R.id.backBtn)


        backBtn.setOnClickListener {
            backToMainActivity(View(this))
        }

        // get the task by ID
        CoroutineScope(Dispatchers.IO).launch {
            val data = repository.getTodoById(taskId)
            title.setText(data.title)
            description.setText(data.description)

        }

        // update the task
        updateBtn.setOnClickListener {
            val title = title.text.toString()
            val description = description.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                repository.update(title, description, taskId)
                val data = repository.getAllTodos()
                //change the context to main because we are updating the ui
                withContext(Dispatchers.Main) {
                    viewModel.setData(data)
                }

            }
            Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show()
            backToMainActivity(View(this@UpdateTodoActivity))

        }


    }



    fun backToMainActivity(v: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}