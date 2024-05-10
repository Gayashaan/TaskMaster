package com.example.taskmaster.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao //todo data access object
interface TodoDao {

    @Insert
    suspend fun insert(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)

    @Update
    suspend fun update(todo: Todo)

    @Query("SELECT * FROM table_todo")
    fun getAllTodos(): List<Todo>


}