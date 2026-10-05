package com.pemmob.todoapp.data.repository

import com.pemmob.todoapp.data.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTodos(): Flow<List<Todo>>
    fun getTodoById(id: String): Flow<Todo?>
    fun addTodo(todo: Todo)
    fun updateTodo(todo: Todo)
    fun deleteTodo(id: String)
    fun toggleTodoCompletion(id: String)
}
