package com.pemmob.todoapp.ui.DetailTugas


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTugasScreen(
    tugasId: String,
    viewModel: DetailViewModel // Terima viewModel sebagai parameter
) {
    // 1. Baca State dari ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // 2. Perintahkan ViewModel untuk mencari data berdasarkan tugasId
    // (Kamu bisa memanggilnya langsung atau menggunakan fungsi bawaan Compose)
    viewModel.getTodoDetail(tugasId)

    // 3. Gunakan 'when' sebagai control flow (Materi Pertemuan 1) untuk render UI
    when (uiState) {
        is DetailUiState.Loading -> {
            // Render indikator loading (misal: CircularProgressIndicator)
        }
        is DetailUiState.Error -> {
            // Render teks error merah
            val errorMessage = (uiState as DetailUiState.Error).message
            Text(text = errorMessage)
        }
        is DetailUiState.Success -> {
            // Keluarkan data 'todo'-nya dari dalam class Success
            val todoData = (uiState as DetailUiState.Success).detail

            // --- MULAI RENDER UI DETAIL DI SINI ---
            // Contoh penggunaan: Text(text = todoData.title)
        }
    }
}

@Preview
@Composable
fun DetailTugasScreenPreview() {
    val navController = rememberNavController()
    DetailTugasScreen(
        tugasId = "1" // 5. Pass data dummy untuk preview
        

    )
}