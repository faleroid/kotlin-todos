package com.pemmob.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pemmob.todoapp.data.repository.InMemoryTodoRepository
import com.pemmob.todoapp.ui.TodoViewModel
import com.pemmob.todoapp.ui.addedit.AddEditTodoScreen
import com.pemmob.todoapp.ui.detail.TodoDetailScreen
import com.pemmob.todoapp.ui.home.HomeScreen
import com.pemmob.todoapp.ui.theme.ToDoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val repository = InMemoryTodoRepository()
        
        enableEdgeToEdge()
        setContent {
            ToDoAppTheme {
                val viewModel: TodoViewModel = viewModel(factory = TodoViewModel.provideFactory(repository))
                val navController = rememberNavController()
                
                NavHost(navController = navController, startDestination = "Home") {
                    composable(route = "Home") {
                        HomeScreen(
                            viewModel = viewModel,
                            onNavigateToAdd = { navController.navigate("add_edit") },
                            onNavigateToDetail = { todoId -> navController.navigate("detail/$todoId") }
                        )
                    }
                    
                    composable(
                        route = "add_edit?todoId={todoId}",
                        arguments = listOf(navArgument(name = "todoId") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        })
                    ) { backStackEntry ->
                        val todoId = backStackEntry.arguments?.getString("todoId")
                        AddEditTodoScreen(
                            todoId = todoId,
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    
                    composable(
                        route = "detail/{todoId}",
                        arguments = listOf(navArgument(name = "todoId") {
                            type = NavType.StringType
                        })
                    ) { backStackEntry ->
                        val todoId = backStackEntry.arguments?.getString("todoId") ?: ""
                        TodoDetailScreen(
                            todoId = todoId,
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToEdit = { id -> navController.navigate("add_edit?todoId=$id") }
                        )
                    }
                }
            }
        }
    }
}