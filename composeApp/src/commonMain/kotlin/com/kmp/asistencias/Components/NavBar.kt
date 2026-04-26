package com.kmp.asistencias.Components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(val title: String, val icon: ImageVector) {
    object Inicio : NavItem("Inicio", Icons.Default.Home)
    object Historial : NavItem("Historial", Icons.Default.History)
    object LectorQR : NavItem("Lector QR", Icons.Default.QrCodeScanner)
    object Reportes : NavItem("Reportes", Icons.AutoMirrored.Filled.Assignment)
    object Perfil : NavItem("Perfil", Icons.Default.Person)
}

@Composable
fun NavBar(
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    val items = listOf(
        NavItem.Inicio,
        NavItem.Historial,
        NavItem.LectorQR,
        NavItem.Reportes,
        NavItem.Perfil
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { 
                    Icon(
                        imageVector = item.icon, 
                        contentDescription = item.title 
                    ) 
                },
                label = { Text(item.title) },
                selected = selectedItem == index,
                onClick = { onItemSelected(index) }
            )
        }
    }
}
