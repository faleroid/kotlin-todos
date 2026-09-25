package com.pemmob.todoapp

import android.app.Application
import com.pemmob.todoapp.data.repository.TodoRepository

class TodoApplication : Application() {
    val repository by lazy { TodoRepository() }
}