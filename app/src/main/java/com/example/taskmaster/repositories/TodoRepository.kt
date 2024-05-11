package com.example.taskmaster.repositories

import com.example.taskmaster.database.TodoDatabase
import com.example.taskmaster.entities.Todo

class TodoRepository(private val db: TodoDatabase) {
    suspend fun insert(todo: Todo) = db.getTodoDao().insert(todo)
    suspend fun delete(todo: Todo) = db.getTodoDao().delete(todo)
    suspend fun update(title: String, description: String, id: Int) = db.getTodoDao().update(title, description, id)
    fun getAllTodos():List<Todo> = db.getTodoDao().getAllTodos()
    fun getTodoById(id: Int): Todo = db.getTodoDao().getTodoById(id)
}