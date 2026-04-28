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
    data object Inicio : NavItem("Inicio", Icons.Default.Home)
    data object Historial : NavItem("Historial", Icons.Default.History)
    data object LectorQR : NavItem("Lector QR", Icons.Default.QrCodeScanner)
    data object Documentos : NavItem("Documentos", Icons.AutoMirrored.Filled.Assignment)
    data object Perfil : NavItem("Perfil", Icons.Default.Person)
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
        NavItem.Documentos,
        NavItem.Perfil
    )

    // Contenedor principal para centrar la píldora
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .height(72.dp),
            shape = CircleShape,
            color = White,
            shadowElevation = 12.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, item ->
                    val isSelected = selectedItem == index
                    
                    Box(
                        modifier = Modifier
                            .height(56.dp)
                            .then(if (isSelected) Modifier.padding(horizontal = 4.dp) else Modifier)
                            .clip(CircleShape)
                            .background(if (isSelected) BlueDeep else Color.Transparent)
                            .clickable { onItemSelected(index) }
                            .padding(horizontal = if (isSelected) 20.dp else 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = if (isSelected) White else Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                            
                            if (isSelected) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = item.title,
                                    fontSize = 14.sp,
                                    color = White,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
