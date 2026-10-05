package com.pemmob.todoapp.data.repository

import com.pemmob.todoapp.data.model.Priority
import com.pemmob.todoapp.data.model.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class InMemoryTodoRepository : TodoRepository {
    // Dummy Data Initializer
    private val _todos = MutableStateFlow<List<Todo>>(
        listOf(
            Todo(
                id = "1",
                title = "Proyek UTS Pemrograman Mobile",
                description = "Membuat desain UI/UX, mengembangkan fitur utama aplikasi, membuat navigasi antarhalaman, melakukan testing dan perbaikan bug, serta menyiapkan laporan dan aplikasi untuk dikumpulkan.",
                isCompleted = false,
                date = "Sep 9, 10.00 AM",
                priority = Priority.MEDIUM
            ),
            Todo(
                id = "2",
                title = "Proyek Pengembangan Game",
                description = "Mendesain karakter, membuat mekanika permainan, dan mengimplementasikan level yang menarik.",
                isCompleted = false,
                date = "Sep 15, 1.00 PM",
                priority = Priority.LOW
            ),
            Todo(
                id = "3",
                title = "Proyek Website E-Commerce",
                description = "Membuat tampilan produk, mengatur proses checkout, dan mengoptimalkan pengalaman pengguna.",
                isCompleted = true,
                date = "Sep 20, 2.30 PM",
                priority = Priority.HIGH
            )
        )
    )

    override fun getTodos(): Flow<List<Todo>> = _todos

    override fun getTodoById(id: String): Flow<Todo?> = _todos.map { todos ->
        todos.find { it.id == id }
    }

    override fun addTodo(todo: Todo) {
        val currentList = _todos.value.toMutableList()
        currentList.add(todo)
        _todos.value = currentList
    }

    override fun updateTodo(todo: Todo) {
        val currentList = _todos.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == todo.id }
        if (index != -1) {
            currentList[index] = todo
            _todos.value = currentList
        }
    }

    override fun deleteTodo(id: String) {
        val currentList = _todos.value.toMutableList()
        currentList.removeAll { it.id == id }
        _todos.value = currentList
    }

    override fun toggleTodoCompletion(id: String) {
        val currentList = _todos.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == id }
        if (index != -1) {
            val todo = currentList[index]
            currentList[index] = todo.copy(isCompleted = !todo.isCompleted)
            _todos.value = currentList
        }
    }
}
