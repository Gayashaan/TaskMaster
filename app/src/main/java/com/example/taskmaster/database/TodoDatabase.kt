package com.example.taskmaster.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.taskmaster.dao.TodoDao
import com.example.taskmaster.entities.Todo


@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase: RoomDatabase() {

   //initialize the dao
    abstract fun getTodoDao(): TodoDao

    //making the class static so that we can access it every where in out project
    // without creating an object of the class
    companion object{
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase{
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }

    }
}
