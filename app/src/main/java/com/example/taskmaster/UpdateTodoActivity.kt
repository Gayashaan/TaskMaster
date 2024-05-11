package com.example.taskmaster

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UpdateTodoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_todo)

        // get task ID from intent extras
        val taskId = intent.getIntExtra("TODO_ID", -1)

        val title: EditText = findViewById(R.id.update_title)
        val description: EditText = findViewById(R.id.update_description)

        // set up the UI components
        title.setText(taskId.toString())


    }
}