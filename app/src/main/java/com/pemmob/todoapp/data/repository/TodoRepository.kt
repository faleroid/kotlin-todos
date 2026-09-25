package com.pemmob.todoapp.data.repository

import com.pemmob.todoapp.data.model.Priority
import com.pemmob.todoapp.data.model.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TodoRepository {
    private val _todos = MutableStateFlow(
        listOf(
            Todo(title = "Belajar Jetpack Compose", description = "Fokus ke State & UDF", priority = Priority.HIGH),
            Todo(title = "Kerjain tugas MVVM", description = "Buat ViewModel + UiState"),
            Todo(title = "Push ke GitHub", isDone = true)
        )
    )
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

    fun addTodo(todo: Todo) {
        _todos.value = _todos.value + todo
    }

    fun updateTodo(updated: Todo) {
        _todos.value = _todos.value.map { if (it.id == updated.id) updated else it }
    }

    fun deleteTodo(id: String) {
        _todos.value = _todos.value.filterNot { it.id == id }
    }

    fun toggleDone(id: String) {
        _todos.value = _todos.value.map {
            if (it.id == id) it.copy(isDone = !it.isDone) else it
        }
    }

    fun getTodoById(id: String): Todo? =
        _todos.value.find { it.id == id }
}