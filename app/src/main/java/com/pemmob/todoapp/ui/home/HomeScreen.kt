package com.pemmob.todoapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pemmob.todoapp.ui.ViewModelFactory

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.pemmob.todoapp.data.model.Todo
import com.pemmob.todoapp.ui.edit.EditTodoBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddClick: () -> Unit,
    onTodoClick: (String) -> Unit = {},
    viewModel: HomeViewModel = viewModel(factory = ViewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentFilter by viewModel.filter.collectAsState()

    // state untuk menyimpan todo yang sedang diedit
    var selectedTodoForEdit by remember { mutableStateOf<Todo?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Todo List") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Todo")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TodoFilter.entries.forEach { filterOption ->
                    FilterChip(
                        selected = currentFilter == filterOption,
                        onClick = { viewModel.onFilterChange(filterOption) },
                        label = { Text(filterOption.name) }
                    )
                }
            }

            when (val state = uiState) {
                is TodoUiState.Loading -> {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is TodoUiState.Error -> {
                    Text(text = "Error: ${state.message}", modifier = Modifier.padding(16.dp))
                }
                is TodoUiState.Success -> {
                    if (state.todos.isEmpty()) {
                        Text(
                            text = "Belum ada todo. Tap + untuk menambah.",
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        LazyColumn {
                            items(state.todos, key = { it.id }) { todo ->
                                TodoItemCard(
                                    todo = todo,
                                    onToggleDone = { viewModel.onToggleDone(todo.id) },
                                    onClick = {
                                        selectedTodoForEdit = todo
                                        onTodoClick(todo.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // modal edit ditampilin di sini karena halaman detail todo belum ada
    selectedTodoForEdit?.let { todoToEdit ->
        EditTodoBottomSheet(
            todo = todoToEdit,
            onDismissRequest = { selectedTodoForEdit = null },
            onUpdateTask = { updated ->
                viewModel.updateTodo(updated)
            }
        )
    }
}
