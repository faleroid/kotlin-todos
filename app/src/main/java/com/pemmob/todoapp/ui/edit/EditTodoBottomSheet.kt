package com.pemmob.todoapp.ui.edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.pemmob.todoapp.data.model.Priority
import com.pemmob.todoapp.data.model.Todo
import com.pemmob.todoapp.ui.theme.BodyLargeMedium
import com.pemmob.todoapp.ui.theme.BodySmallRegular
import com.pemmob.todoapp.ui.theme.H1Bold
import com.pemmob.todoapp.ui.theme.TaskGreen
import com.pemmob.todoapp.ui.theme.ToDoAppTheme
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

private val FieldBackground = Color.White
private val LabelGray = Color(0xFF212121)
private val BorderGray = Color(0xFFCCCCCC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTodoBottomSheet(
    todo: Todo,
    onDismissRequest: () -> Unit,
    onUpdateTask: (Todo) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.White,
        modifier = modifier
    ) {
        EditTodoSheetContent(
            todo = todo,
            onDismissRequest = onDismissRequest,
            onUpdateTask = onUpdateTask
        )
    }
}

/**
 * isi form dipisah biar bisa ditampilkan langsung di compose preview
 */
@Composable
fun EditTodoSheetContent(
    todo: Todo,
    onDismissRequest: () -> Unit,
    onUpdateTask: (Todo) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by rememberSaveable(todo.id) { mutableStateOf(todo.title) }
    var description by rememberSaveable(todo.id) { mutableStateOf(todo.description) }
    var priority by rememberSaveable(todo.id) { mutableStateOf(todo.priority) }
    var dueFrom by rememberSaveable(todo.id) { mutableStateOf(todo.dueFrom) }
    var dueTo by rememberSaveable(todo.id) { mutableStateOf(todo.dueTo) }
    var isDone by rememberSaveable(todo.id) { mutableStateOf(todo.isDone) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // judul form
        Text(
            text = "Edit Task",
            color = TaskGreen,
            style = H1Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalDivider(
            color = TaskGreen,
            thickness = 2.dp,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 20.dp)
                .fillMaxWidth(0.85f)
        )

        // jarak antarbagian form dibuat seragam
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // bagian juduul tugas
            Column {
                FieldLabel("Task title")
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Masukkan judul tugas", style = BodySmallRegular) },
                    isError = title.isBlank(),
                    textStyle = BodySmallRegular,
                    colors = fieldColors(),
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // deskripsi tugas
            Column {
                FieldLabel("Description")
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Tulis deskripsi tugas...", style = BodySmallRegular) },
                    textStyle = BodySmallRegular,
                    colors = fieldColors(),
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }

            // pilihan prioritas sesuai yang di model
            Column {
                FieldLabel("Priority Level")
                Spacer(Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Priority.entries.forEach { level ->
                        val label = level.name.lowercase().replaceFirstChar { it.uppercase() }
                        val selected = priority == level

                        OutlinedButton(
                            onClick = { priority = level },
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (selected) TaskGreen else BorderGray
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (selected) TaskGreen else Color.DarkGray,
                                containerColor = Color.White
                            ),
                            shape = MaterialTheme.shapes.small,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = label,
                                style = if (selected) BodySmallRegular.copy(fontWeight = FontWeight.Bold) else BodySmallRegular
                            )
                        }
                    }
                }
            }

            // tanggal mulai dan selesai
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DateTimeField(
                    label = "From",
                    value = dueFrom,
                    onValueSelected = { dueFrom = it },
                    modifier = Modifier.weight(1f)
                )
                DateTimeField(
                    label = "To",
                    value = dueTo,
                    onValueSelected = { dueTo = it },
                    modifier = Modifier.weight(1f)
                )
            }

            // status tugas
            Column {
                FieldLabel("Status")
                StatusDropdown(
                    isDone = isDone,
                    onStatusSelected = { isDone = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(12.dp))

            // salin nilai form ke tugas, lalu tutup lembar edit
            Button(
                onClick = {
                    val updated = todo.copy(
                        title = title,
                        description = description,
                        priority = priority,
                        dueFrom = dueFrom,
                        dueTo = dueTo,
                        isDone = isDone
                    )
                    onUpdateTask(updated)
                    onDismissRequest()
                },
                enabled = title.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = TaskGreen),
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Update Task",
                    style = BodyLargeMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatusDropdown(
    isDone: Boolean,
    onStatusSelected: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("To do" to false, "Completed" to true)
    val selectedOptionText = if (isDone) "Completed" else "To do"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedOptionText,
            onValueChange = {},
            readOnly = true,
            textStyle = BodySmallRegular,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Pilih Status",
                    tint = TaskGreen
                )
            },
            colors = fieldColors(),
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (label, statusValue) ->
                DropdownMenuItem(
                    text = { Text(label, style = BodySmallRegular) },
                    onClick = {
                        onStatusSelected(statusValue)
                        expanded = false
                    }
                )
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
            textStyle = BodySmallRegular,
            placeholder = { Text("Pilih", style = BodySmallRegular) },
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Pilih tanggal & jam",
                        tint = Color.DarkGray
                    )
                }
            },
            colors = fieldColors(),
            shape = MaterialTheme.shapes.small,
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
                }) { Text("Lanjut pilih jam", style = BodySmallRegular) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Batal", style = BodySmallRegular) }
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
                            DateTimeFormatter.ofPattern("MMM d, h.mm a", Locale.ENGLISH)
                        )
                        onValueSelected(formatted)
                    }
                    showTimePicker = false
                }) { Text("Selesai", style = BodySmallRegular) }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Batal", style = BodySmallRegular) }
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
        style = BodyLargeMedium,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = FieldBackground,
    unfocusedContainerColor = FieldBackground,
    focusedBorderColor = TaskGreen,
    unfocusedBorderColor = BorderGray
)

// preview form dengan data contoh.
@Preview(showBackground = true, heightDp = 680)
@Composable
fun EditTodoBottomSheetPreview() {
    ToDoAppTheme {
        val sampleTodo = Todo(
            id = "1",
            title = "Proyek UTS Pemrograman Mobile",
            description = "Membuat desain UI/UX, mengembangkan fitur utama aplikasi, membuat navigasi antarhalaman, melakukan testing dan perbaikan bug, serta menyiapkan laporan dan aplikasi untuk dikumpulkan.",
            priority = Priority.MEDIUM,
            dueFrom = "Sep 6, 10.00 AM",
            dueTo = "Sep 6, 12.99 AM",
            isDone = false
        )

        Surface(color = Color.White) {
            EditTodoSheetContent(
                todo = sampleTodo,
                onDismissRequest = {},
                onUpdateTask = {}
            )
        }
    }
}
