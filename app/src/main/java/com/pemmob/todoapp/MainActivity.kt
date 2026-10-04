package com.pemmob.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pemmob.todoapp.ui.DetailTugas.DetailTugasScreen
import com.pemmob.todoapp.ui.home.HomeScreen
import com.pemmob.todoapp.ui.theme.ToDoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "Home") {
                    composable(route = "Home") {
                        HomeScreen(
                            // Berpindah ke layar tambah tugas (jika nanti ada)
                            onAddClick = { /* navController.navigate("add_tugas") */ },
                            // Berpindah ke layar detail dengan membawa ID tugas
                            onTodoClick = { tugasId -> navController.navigate("detail/$tugasId") }
                        )
                    }

//                    composable(route = "Home") {
//                        HomeScreen(
//                            navController = navController,
//                            viewModel = HomeScreen()
//                        )
//                    }
                    composable(
                        route = "detail/{tugasId}", // ❌ Seharusnya 'tugasId', bukan 'productId'
                        arguments = listOf(navArgument(name = "productId") { // ❌ ERROR: navArgument belum di-import
                            type = NavType.IntType // ❌ ERROR: NavType belum di-import
                        })
                    ) { backStackEntry ->
                        val tugasId = backStackEntry.arguments?.getInt("tugasId") ?: 0
                        DetailTugasScreen( // ❌ ERROR: File ini tidak ada, harusnya 'DetailTugasScreen'
                            tugasId = tugasId,
                            )
                    }

//                    composable(
//                        route = "detail/{productId}",
//                        arguments = listOf(navArgument(name = "productId") {
//                            type = NavType.IntType
//                        })
//                    ) { backStackEntry ->
//                        val productId = backStackEntry.arguments?.getInt("productId") ?: 0
//                        DetailProductScreen(
//                            productId = productId,
//                            navController = navController,
//                            viewModel = productViewModel
//                        )
//                    }
                }
            }
        }
    }
}