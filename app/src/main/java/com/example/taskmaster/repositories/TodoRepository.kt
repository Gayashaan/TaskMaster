package com.example.taskmaster.repositories

import com.example.taskmaster.database.TodoDatabase
import com.example.taskmaster.entities.Todo

class TodoRepository(private val db: TodoDatabase) {
    suspend fun insert(todo: Todo) = db.getTodoDao().insert(todo)
    suspend fun delete(todo: Todo) = db.getTodoDao().delete(todo)
    suspend fun update(title: String, description: String, id: Int) = db.getTodoDao().update(title, description, id)
    suspend fun getAllTodos():List<Todo> = db.getTodoDao().getAllTodos()
    suspend fun getTodoById(id: Int): Todo = db.getTodoDao().getTodoById(id)
    suspend fun getTodosCount(): Int = db.getTodoDao().getTodosCount()
    suspend fun getCompletedTodos(): List<Todo> = db.getTodoDao().getCompletedTodos()
    suspend fun getUncompletedTodos(): List<Todo> = db.getTodoDao().getUncompletedTodos()
    suspend fun markAsCompleted(id: Int) = db.getTodoDao().markAsCompleted(id)
    suspend fun markAsInComplete(id: Int) = db.getTodoDao().markAsUncompleted(id)

}