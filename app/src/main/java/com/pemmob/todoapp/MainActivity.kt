package com.pemmob.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pemmob.todoapp.ui.add.AddTodoScreen
import com.pemmob.todoapp.ui.home.HomeScreen
import com.pemmob.todoapp.ui.theme.ToDoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoAppTheme {
                TodoNavGraph()
            }
        }
    }
}

@Composable
fun TodoNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onAddClick = { navController.navigate("add_todo") },
                onTodoClick = { /* nanti untuk detail/edit todo */ }
            )
        }
        composable("add_todo") {
            AddTodoScreen(onDone = { navController.popBackStack() })
        }
    }
}