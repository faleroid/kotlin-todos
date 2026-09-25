package com.pemmob.todoapp.data.model

import java.util.UUID

data class Todo(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val isDone: Boolean = false,
    val priority: Priority = Priority.MEDIUM
)

enum class Priority { LOW, MEDIUM, HIGH }