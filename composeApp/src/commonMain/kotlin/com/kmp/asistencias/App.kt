package com.kmp.asistencias

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kmp.asistencias.Components.NavBar
import com.kmp.asistencias.Views.Documentos
import com.kmp.asistencias.Views.Historial
import com.kmp.asistencias.Views.Home
import com.kmp.asistencias.Views.Perfil

@Composable
fun App() {
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavBar(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedItem) {
                0 -> Home()
                1 -> Historial()
                2 -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Lector QR") }
                3 -> Documentos()
                4 -> Perfil()
            }
        }
    }
}
