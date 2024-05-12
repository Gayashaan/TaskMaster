package com.example.taskmaster.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmaster.entities.Todo

class MainActivityData: ViewModel() {
    private val _count = MutableLiveData<Int>()
    private val _countCompleted = MutableLiveData<Int>()
    private val _data = MutableLiveData<List<Todo>>()
    private val _completed = MutableLiveData<List<Todo>>()

    val data: LiveData<List<Todo>> = _data
    val count: LiveData<Int> = _count
    val completed: LiveData<List<Todo>> = _completed
    val completedCount: LiveData<Int> = _countCompleted

    fun setData(data: List<Todo>){
        _data.value = data
    }

    fun setCount(count: Int){
        _count.value = count
    }

    fun setCompletedCount(count: Int){
        _countCompleted.value = count
    }

    fun setCompleted(data: List<Todo>){
        _completed.value = data
    }



}