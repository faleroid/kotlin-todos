package com.pemmob.todoapp.ui.DetailTugas

import androidx.lifecycle.ViewModel
import com.pemmob.todoapp.data.model.Todo
import com.pemmob.todoapp.data.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface DetailUiState {

    object Loading : DetailUiState

    data class Success(
        val detail: Todo
    ) : DetailUiState

    data class Error(val message: String) : DetailUiState

}

// Pastikan ViewModel menerima Repository
class DetailViewModel(
    private val repository: TodoRepository
) : ViewModel() {

    // 1. Buat penampung State (Private)
    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)

    // 2. Ekspos menjadi StateFlow agar bisa dibaca UI secara aman
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    // 3. Fungsi untuk mengambil data spesifik berdasarkan ID
    fun getTodoDetail(id: String) {
        val todo = repository.getTodoById(id) // Ambil dari "Local DB"

        if (todo != null) {
            _uiState.value = DetailUiState.Success(todo) // Update UI jadi Sukses
        } else {
            _uiState.value = DetailUiState.Error("Tugas tidak ditemukan") // Update UI jadi Error
        }
    }
}
