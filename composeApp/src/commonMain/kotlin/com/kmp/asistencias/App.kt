package com.kmp.asistencias

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
                TopBar(
                    title = titles[selectedItem],
                    onBackClick = if (selectedItem != 0) {
                        { selectedItem = 0 }
                    } else null
                )
            }
        ) { paddingValues ->
            val currentBgColor = if (selectedItem == 4) Color(0xFFF2F2F7) else BackgroundWhite

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(currentBgColor)
                    .padding(top = paddingValues.calculateTopPadding())
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.weight(1f)) {
                        when (selectedItem) {
                            0 -> Home(onNavigateToHistory = { selectedItem = 1 })
                            1 -> Historial()
                            2 -> Documentos()
                            3 -> Perfil(onLogout = { isLoggedIn = false })
                        }
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
