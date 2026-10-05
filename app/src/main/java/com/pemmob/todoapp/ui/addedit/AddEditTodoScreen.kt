package com.pemmob.todoapp.ui.addedit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pemmob.todoapp.data.model.Priority
import com.pemmob.todoapp.data.model.Todo
import com.pemmob.todoapp.ui.TodoViewModel
import com.pemmob.todoapp.ui.theme.AmberNormal
import com.pemmob.todoapp.ui.theme.AppBackground
import com.pemmob.todoapp.ui.theme.GreenNormal
import com.pemmob.todoapp.ui.theme.Neutral
import com.pemmob.todoapp.ui.theme.TextPrimary
import com.pemmob.todoapp.ui.theme.TextSecondary
import com.pemmob.todoapp.ui.theme.White
import java.util.Calendar
import java.util.Locale
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTodoScreen(
    todoId: String?,
    viewModel: TodoViewModel,
    onNavigateBack: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }
    var priority by rememberSaveable { mutableStateOf(Priority.LOW) }
    var isCompleted by rememberSaveable { mutableStateOf(false) }
    val formFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = TextPrimary,
        unfocusedTextColor = TextPrimary,
        focusedContainerColor = White,
        unfocusedContainerColor = White,
        focusedBorderColor = GreenNormal,
        unfocusedBorderColor = Neutral,
        cursorColor = GreenNormal,
        disabledTextColor = TextPrimary,
        disabledContainerColor = White,
        disabledBorderColor = Neutral,
        disabledLabelColor = TextSecondary
    )
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val showDateTimePicker = {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        val selectedDateTime = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth, hourOfDay, minute)
                        }
                        date = SimpleDateFormat(
                            "EEEE, d MMMM yyyy '•' HH:mm",
                            Locale("id", "ID")
                        ).format(selectedDateTime.time)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    val todoState by (if (todoId != null) viewModel.getTodoById(todoId) else kotlinx.coroutines.flow.flowOf(null)).collectAsState(initial = null)

    LaunchedEffect(todoState) {
        todoState?.let {
            title = it.title
            description = it.description
            date = it.date
            priority = it.priority
            isCompleted = it.isCompleted
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (todoId == null) "Add Task" else "Edit Task", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBackground,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary
                )
            )
        },
        containerColor = AppBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            Text("Task title", fontSize = 14.sp, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = formFieldColors
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Description", fontSize = 14.sp, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(8.dp),
                maxLines = 4,
                colors = formFieldColors
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Priority Level", fontSize = 14.sp, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                PriorityChip(
                    text = "Low",
                    selected = priority == Priority.LOW,
                    onClick = { priority = Priority.LOW },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                PriorityChip(
                    text = "Medium",
                    selected = priority == Priority.MEDIUM,
                    onClick = { priority = Priority.MEDIUM },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                PriorityChip(
                    text = "High",
                    selected = priority == Priority.HIGH,
                    onClick = { priority = Priority.HIGH },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Date / Time", fontSize = 14.sp, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = showDateTimePicker)
            ) {
                OutlinedTextField(
                    value = date,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    enabled = false,
                    label = { Text("Select date and time") },
                    colors = formFieldColors
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val newTodo = Todo(
                            id = todoState?.id ?: java.util.UUID.randomUUID().toString(),
                            title = title,
                            description = description,
                            date = date,
                            priority = priority,
                            isCompleted = isCompleted
                        )
                        if (todoId == null) {
                            viewModel.addTodo(newTodo)
                        } else {
                            viewModel.updateTodo(newTodo)
                        }
                        onNavigateBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenNormal),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(if (todoId == null) "Save Task" else "Update Task", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun PriorityChip(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val borderColor = if (selected) AmberNormal else Neutral
    val textColor = if (selected) AmberNormal else TextPrimary
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Text(text = text, color = textColor)
    }
}
