package com.kmp.asistencias.Components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kmp.asistencias.Themes.BlueDeep
import com.kmp.asistencias.Themes.White

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
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem.Inicio,
        NavItem.Historial,
        NavItem.LectorQR,
        NavItem.Reportes,
        NavItem.Perfil
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .height(80.dp),
        shape = CircleShape,
        color = White,
        shadowElevation = 15.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedItem == index
                
                Box(
                    modifier = Modifier
                        .size(if (isSelected) 64.dp else 50.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) BlueDeep else Color.Transparent)
                        .clickable { onItemSelected(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (isSelected) White else Color.Gray,
                            modifier = Modifier.size(if (isSelected) 28.dp else 24.dp)
                        )
                        if (!isSelected) {
                            Text(
                                text = item.title,
                                fontSize = 10.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
