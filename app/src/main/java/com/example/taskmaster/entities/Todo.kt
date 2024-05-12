package com.example.taskmaster.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Todo")
data class Todo(var title: String?, var description: String?, var isCompleted: Int = 0){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
