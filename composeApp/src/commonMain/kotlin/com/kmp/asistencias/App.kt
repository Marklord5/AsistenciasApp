package com.kmp.asistencias

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kmp.asistencias.Components.NavBar
import com.kmp.asistencias.Components.TopBar
import com.kmp.asistencias.Themes.BackgroundWhite
import com.kmp.asistencias.Views.Documentos
import com.kmp.asistencias.Views.Historial
import com.kmp.asistencias.Views.Home
import com.kmp.asistencias.Views.Login
import com.kmp.asistencias.Views.Perfil

@Composable
fun App() {
    var isLoggedIn by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(0) }
    val titles = listOf("Inicio", "Historial", "Lector QR", "Reportes", "Perfil")

    if (!isLoggedIn) {
        Login(onLoginSuccess = { isLoggedIn = true })
    } else {
        Scaffold(
            containerColor = BackgroundWhite,
            topBar = {
                TopBar(title = titles[selectedItem])
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
            ) {
                // Añadimos padding inferior aquí (100dp aprox para librar la píldora)
                Box(modifier = Modifier
                    .fillMaxSize()
                ) {
                    when (selectedItem) {
                        0 -> Home()
                        1 -> Historial()
                        2 -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Lector QR") }
                        3 -> Documentos()
                        4 -> Perfil(onLogout = { isLoggedIn = false })
                    }
                }
                
                NavBar(
                    selectedItem = selectedItem,
                    onItemSelected = { selectedItem = it },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}
