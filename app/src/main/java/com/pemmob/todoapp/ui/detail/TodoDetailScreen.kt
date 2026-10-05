package com.pemmob.todoapp.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pemmob.todoapp.ui.TodoViewModel
import com.pemmob.todoapp.ui.theme.AmberNormal
import com.pemmob.todoapp.ui.theme.AppBackground
import com.pemmob.todoapp.ui.theme.GreenDark
import com.pemmob.todoapp.ui.theme.GreenDarker
import com.pemmob.todoapp.ui.theme.GreenNormal
import com.pemmob.todoapp.ui.theme.Neutral
import com.pemmob.todoapp.ui.theme.TextPrimary
import com.pemmob.todoapp.ui.theme.TextSecondary
import com.pemmob.todoapp.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailScreen(
    todoId: String,
    viewModel: TodoViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit
) {
    val todo by viewModel.getTodoById(todoId).collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Detail Tugas", fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth()) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBackground)
            )
        },
        floatingActionButton = {
            if (todo != null) {
                FloatingActionButton(
                    onClick = { onNavigateToEdit(todo!!.id) },
                    containerColor = GreenNormal,
                    contentColor = White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit Todo")
                }
            }
        },
        containerColor = AppBackground
    ) { innerPadding ->
        if (todo != null) {
            val currentTodo = todo!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            // Priority Badge
                            val priorityColor = AmberNormal
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(1.dp, priorityColor, RoundedCornerShape(16.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = currentTodo.priority.name.lowercase().replaceFirstChar { it.uppercase() },
                                    fontSize = 12.sp,
                                    color = priorityColor,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            // Status Badge
                            val statusText = if (currentTodo.isCompleted) "Completed" else "In Progres"
                            val statusColor = GreenNormal
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(statusColor)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = statusText,
                                    fontSize = 12.sp,
                                    color = White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = currentTodo.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentTodo.description,
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Date Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(AppBackground)
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Done, contentDescription = "Date", tint = Neutral, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = currentTodo.date, fontSize = 14.sp, color = TextSecondary)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Actions (Complete / Delete)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Button(
                        onClick = { viewModel.toggleTodoCompletion(currentTodo.id) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = if (currentTodo.isCompleted) Neutral else GreenNormal),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Icon(Icons.Filled.Done, contentDescription = "Complete", tint= GreenDarker)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (currentTodo.isCompleted) "Mark as Uncompleted" else "Mark as Completed")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            viewModel.deleteTodo(currentTodo.id)
                            onNavigateBack()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete", tint=White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Delete", color=White)
                    }
                }
            }
        }
    }
}
