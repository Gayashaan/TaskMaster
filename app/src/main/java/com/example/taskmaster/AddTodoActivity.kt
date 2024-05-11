package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.taskmaster.database.TodoDatabase
import com.example.taskmaster.entities.Todo
import com.example.taskmaster.repositories.TodoRepository
import com.example.taskmaster.viewmodel.MainActivityData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTodoActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityData
    private lateinit var title: EditText
    private lateinit var description: EditText
    private lateinit var addBtn: Button
    private lateinit var backBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_todo)

        val repository = TodoRepository(TodoDatabase.getInstance(this))

        viewModel = ViewModelProvider(this)[MainActivityData::class.java]

        title = findViewById(R.id.titleInput)
        description = findViewById(R.id.descriptionInput)
        addBtn = findViewById(R.id.addTaskBtn)
        backBtn = findViewById(R.id.btnBack)

        backBtn.setOnClickListener {
            backToMainActivity(View(this))
        }


        addBtn.setOnClickListener {
            addTodoItem(title.text.toString(), repository)
        }

    }


    fun backToMainActivity(v: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun addTodoItem(item: String, repository: TodoRepository) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insert(Todo(item))
            backToMainActivity(View(this@AddTodoActivity))
        }
    }
}