package com.pemmob.todoapp.data.model

import java.util.UUID

enum class Priority {
    LOW, MEDIUM, HIGH
}

data class Todo(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val date: String,
    val priority: Priority
)