package com.example.taskmaster.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_todo")
data class Todo(
    var item: String?
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
