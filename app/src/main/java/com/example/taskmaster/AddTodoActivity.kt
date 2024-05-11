package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.view.View
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

class AddTodoActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_todo)

        val repository = TodoRepository(TodoDatabase.getInstance(this))

        viewModel = ViewModelProvider(this)[MainActivityData::class.java]


        CoroutineScope(Dispatchers.IO).launch {
            repository.insert(Todo(item))
            val data = repository.getAllTodos()
            runOnUiThread() {
                viewModel.setData(data)
            }
        }
    }


    fun backToMainActivity(v: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}