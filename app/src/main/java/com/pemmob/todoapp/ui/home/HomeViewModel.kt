package com.pemmob.todoapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.todoapp.data.model.Todo
import com.pemmob.todoapp.data.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

enum class TodoFilter { ALL, ACTIVE, COMPLETED }

class HomeViewModel(
    private val repository: TodoRepository
) : ViewModel() {

    private val _filter = MutableStateFlow(TodoFilter.ALL)
    val filter: StateFlow<TodoFilter> = _filter

    val uiState: StateFlow<TodoUiState> = repository.todos
        .map { todos ->
            val filtered = when (_filter.value) {
                TodoFilter.ALL -> todos
                TodoFilter.ACTIVE -> todos.filterNot { it.isDone }
                TodoFilter.COMPLETED -> todos.filter { it.isDone }
            }
            TodoUiState.Success(filtered) as TodoUiState
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TodoUiState.Loading
        )

    fun onToggleDone(id: String) {
        repository.toggleDone(id)
    }

    fun onDeleteTodo(id: String) {
        repository.deleteTodo(id)
    }

    fun onFilterChange(newFilter: TodoFilter) {
        _filter.value = newFilter
    }
}