package com.pemmob.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pemmob.todoapp.ui.home.HomeScreen
import com.pemmob.todoapp.ui.theme.ToDoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoAppTheme {
                HomeScreen(
                    onAddClick = { /* navigasi */ },
                    onTodoClick = { /* navigasi */ }
                )
            }
        }
    }
}