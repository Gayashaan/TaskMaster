package com.example.taskmaster.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskmaster.entities.Todo


@Dao
interface TodoDao {

    @Insert
    suspend fun insert(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)

    @Query("UPDATE Todo SET title = :title, description = :description WHERE id = :id")
    suspend fun update(title: String, description: String, id: Int)

    @Query("SELECT * FROM Todo ORDER BY id DESC")
    suspend fun getAllTodos(): List<Todo>

    @Query("SELECT * FROM Todo WHERE id = :id")
    suspend fun getTodoById(id: Int): Todo

    @Query("SELECT COUNT(*) FROM Todo")
    suspend fun getTodosCount(): Int

    @Query("SELECT * FROM Todo WHERE isCompleted = 1")
    suspend fun getCompletedTodos(): List<Todo>

    @Query("SELECT * FROM Todo WHERE isCompleted = 0")
    suspend fun getUncompletedTodos(): List<Todo>

    @Query("UPDATE Todo SET isCompleted = 1 WHERE id = :id")
    suspend fun markAsCompleted(id: Int)

    @Query("UPDATE Todo SET isCompleted = 0 WHERE id = :id")
    suspend fun markAsUncompleted(id: Int)


}