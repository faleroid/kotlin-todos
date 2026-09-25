package com.pemmob.todoapp.ui.home

import com.pemmob.todoapp.data.model.Todo

sealed interface TodoUiState {
    data object Loading : TodoUiState
    data class Success(val todos: List<Todo>) : TodoUiState
    data class Error(val message: String) : TodoUiState
}