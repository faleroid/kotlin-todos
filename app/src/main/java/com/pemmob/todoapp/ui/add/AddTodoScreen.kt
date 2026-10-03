package com.pemmob.todoapp.ui.add

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pemmob.todoapp.data.model.Priority
import com.pemmob.todoapp.ui.ViewModelFactory
import com.pemmob.todoapp.ui.home.HomeViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

private val TaskGreen = Color(0xFF58CC02)
private val FieldBackground = Color(0xFFF3F5F9)
private val LabelGray = Color(0xFF555657)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoScreen(
    onDone: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = ViewModelFactory)
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var dueFrom by rememberSaveable { mutableStateOf("") }
    var dueTo by rememberSaveable { mutableStateOf("") }
    var priority by rememberSaveable { mutableStateOf(Priority.MEDIUM) }

    StatelessAddTodoForm(
        title = title, onTitleChange = { title = it },
        description = description, onDescriptionChange = { description = it },
        location = location, onLocationChange = { location = it },
        dueFrom = dueFrom, onDueFromChange = { dueFrom = it },
        dueTo = dueTo, onDueToChange = { dueTo = it },
        priority = priority, onPriorityChange = { priority = it },
        isFormValid = title.isNotBlank(),
        onBackClick = onDone,
        onSubmit = {
            viewModel.addTodo(title, description, location, dueFrom, dueTo, priority)
            onDone()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessAddTodoForm(
    title: String, onTitleChange: (String) -> Unit,
    description: String, onDescriptionChange: (String) -> Unit,
    location: String, onLocationChange: (String) -> Unit,
    dueFrom: String, onDueFromChange: (String) -> Unit,
    dueTo: String, onDueToChange: (String) -> Unit,
    priority: Priority, onPriorityChange: (Priority) -> Unit,
    isFormValid: Boolean,
    onBackClick: () -> Unit,
    onSubmit: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Task", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .fillMaxSize()
        ) {
            FieldLabel("Task title")
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                placeholder = { Text("Masukkan judul tugas") },
                isError = title.isBlank(),
                colors = fieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))
            FieldLabel("Description")
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                placeholder = { Text("Tulis deskripsi tugas...") },
                colors = fieldColors(),
                modifier = Modifier.fillMaxWidth().height(110.dp)
            )

            Spacer(Modifier.height(16.dp))
            FieldLabel("Location")
            OutlinedTextField(
                value = location,
                onValueChange = onLocationChange,
                placeholder = { Text("mis. Asynchronous") },
                colors = fieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))
            FieldLabel("Priority Level")
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Priority.entries.forEach { level ->
                    val label = level.name.lowercase().replaceFirstChar { it.uppercase() }
                    FilterChip(
                        selected = priority == level,
                        onClick = { onPriorityChange(level) },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TaskGreen,
                            selectedLabelColor = Color.White,
                            containerColor = FieldBackground,
                            labelColor = LabelGray
                        )
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DateTimeField(
                    label = "From",
                    value = dueFrom,
                    onValueSelected = onDueFromChange,
                    modifier = Modifier.weight(1f)
                )
                DateTimeField(
                    label = "To",
                    value = dueTo,
                    onValueSelected = onDueToChange,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(28.dp))
            Button(
                onClick = onSubmit,
                enabled = isFormValid,
                colors = ButtonDefaults.buttonColors(containerColor = TaskGreen),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Add Task", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateTimeField(
    label: String,
    value: String,
    onValueSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var pickedDateMillis by remember { mutableStateOf<Long?>(null) }

    Column(modifier = modifier) {
        FieldLabel(label)
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Pilih") },
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Pilih tanggal & jam")
                }
            },
            colors = fieldColors(),
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickedDateMillis = datePickerState.selectedDateMillis
                    showDatePicker = false
                    showTimePicker = true
                }) { Text("Lanjut pilih jam") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Batal") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(is24Hour = false)
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = pickedDateMillis
                    if (millis != null) {
                        val date = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                        val time = LocalTime.of(timePickerState.hour, timePickerState.minute)
                        val dateTime = LocalDateTime.of(date, time)
                        val formatted = dateTime.format(
                            DateTimeFormatter.ofPattern("MMM d, h:mm a", Locale.ENGLISH)
                        )
                        onValueSelected(formatted)
                    }
                    showTimePicker = false
                }) { Text("Selesai") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Batal") }
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }
}

@Composable
private fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    dismissButton()
                    Spacer(Modifier.width(8.dp))
                    confirmButton()
                }
            }
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        color = LabelGray,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = FieldBackground,
    unfocusedContainerColor = FieldBackground,
    focusedBorderColor = TaskGreen,
    unfocusedBorderColor = Color.Transparent
)