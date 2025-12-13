package com.example.tempcool_remake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tempcool_remake.Vistas.*
import com.example.tempcool_remake.ui.theme.TempCool_remakeTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class MainActivity : ComponentActivity() {

    private lateinit var authFB: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            authFB = Firebase.auth
            TempCool_remakeTheme {
                val navController = rememberNavController()

                val vistaVisita = listOf("home", "login", "register")

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val vistaActual = navBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (vistaActual !in vistaVisita) {
                            BottomNavigationBar(
                                currentRoute = vistaActual,
                                onNavigate = { route ->
                                    navController.navigate(route) {
                                        launchSingleTop = true
                                        popUpTo("home") { inclusive = false }
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(
                            navController = navController,
                            startDestination = "home"
                        ) {
                            // Pantallas sin Bottom Bar
                            composable("home") { Home(navController) }
                            composable("login") { Login(navController, authFB) }
                            composable("register") { Register(navController, authFB) }

                            // Pantallas con Bottom Bar
                            composable("options") { Opciones(navController) }
                            composable("dataTemp") { TempScreen(navController) }
                            composable("listTemp") { HistorialScreen(navController) }
                            composable("profile") { PerfilUser(navController) }
                        }
                    }
                }
            }
        }
    }
}

// Barra de navegacion
@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {

    NavigationBar (
    ) {
        val items = listOf(
            BottomItem("options", "Opciones", Icons.Filled.Settings),
            BottomItem("dataTemp", "Temperatura", Icons.Filled.Place),
            BottomItem("listTemp", "Historial", Icons.Filled.Menu),
            BottomItem("profile", "Mi perfil", Icons.Filled.Person),
        )


        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

data class BottomItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
