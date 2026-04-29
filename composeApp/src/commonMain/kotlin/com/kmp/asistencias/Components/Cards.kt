package com.kmp.asistencias.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kmp.asistencias.Models.PerfilUsuarioResponse

/**
 * Componente para mostrar estadísticas como "Horas Hoy" o "Puntualidad".
 */
@Composable
fun StatCard(
    icon: ImageVector,
    label: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White)
            .padding(24.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF8E8E93),
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = label,
                    color = Color(0xFF8E8E93),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )) {
                        append(value)
                    }
                    withStyle(style = SpanStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8E8E93)
                    )) {
                        append(unit)
                    }
                }
            )
        }
    }
}

/**
 * Componente de tarjeta para actividad reciente simplificada (Usada en Home).
 */
@Composable
fun HomeActivityCard(
    title: String,
    subtitle: String,
    time: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF1F1F1)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF8E8E93),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color.Black
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = Color(0xFF8E8E93)
                )
            }
            
            Text(
                text = time,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF8E8E93)
            )
        }
    }
}

/**
 * Componente para un item individual del historial reciente con línea de tiempo.
 */
@Composable
fun HistoryItem(
    isEntry: Boolean,
    title: String,
    time: String,
    location: String,
    isLast: Boolean = false,
    modifier: Modifier = Modifier
) {
    val icon = if (isEntry) Icons.Default.Login else Icons.Default.Logout
    val bgColor = if (isEntry) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
    val iconColor = if (isEntry) Color(0xFF4CAF50) else Color(0xFFF57C00)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // Columna de la línea de tiempo
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(42.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(Color(0xFFEEEEEE))
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Contenido del texto
        Column(
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Hora
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Color(0xFF8E8E93),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = time,
                        color = Color(0xFF8E8E93),
                        fontSize = 13.sp
                    )
                }

                // Separador punto
                Text(
                    text = "•",
                    color = Color(0xFF8E8E93),
                    fontSize = 13.sp
                )

                // Ubicación
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (location.lowercase() == "remoto") 
                            Icons.Default.Schedule else Icons.Default.LocationOn, // Placeholder for home icon
                        contentDescription = null,
                        tint = Color(0xFF8E8E93),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = location,
                        color = Color(0xFF8E8E93),
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

/**
 * Componente completo de la sección de Historial Reciente.
 */
@Composable
fun RecentHistorySection(
    registros: List<PerfilUsuarioResponse.UltimoRegistro> = emptyList(),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(Color.White)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Historial Reciente",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            /*Text(
                text = "Ver todo",
                fontSize = 14.sp,
                color = Color.Gray
            )*/
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        if (registros.isEmpty()) {
            Text(
                text = "No hay registros recientes",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            registros.forEachIndexed { index, registro ->
                // Simple parsing for display
                val isEntry = registro.tipo.contains("Entrada", ignoreCase = true)
                val displayTime = try {
                    registro.fechaHora.split("T").last().substring(0, 5)
                } catch (e: Exception) {
                    registro.fechaHora
                }
                
                val displayDate = try {
                    registro.fechaHora.split("T").first()
                } catch (e: Exception) {
                    ""
                }

                HistoryItem(
                    isEntry = isEntry,
                    title = "${registro.tipo} - $displayDate",
                    time = displayTime,
                    location = "Ubicación registrada", // El modelo no trae ubicación por ahora
                    isLast = index == registros.size - 1
                )
            }
        }
    }
}

/**
 * Componente de tarjeta para el botón de cerrar sesión.
 */
@Composable
fun LogoutCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Cerrar Sesión",
                tint = Color(0xFFFF3B30), // Color rojo para resaltar la acción
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Cerrar Sesión",
                color = Color(0xFFFF3B30),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


/*
Cards de Historial
 */

@Composable
fun HistorySectionHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        if (subtitle != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF1F1F1))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF8E8E93),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AttendanceHistoryCard(
    startTime: String,
    endTime: String,
    location: String,
    locationIcon: ImageVector,
    status: String,
    statusColor: Color,
    statusTextColor: Color = Color.Black,
    mainIcon: ImageVector,
    mainIconColor: Color,
    mainIconBgColor: Color,
    extraBadgeText: String? = null,
    extraBadgeColor: Color = Color(0xFFF1F1F1),
    extraBadgeTextColor: Color = Color(0xFF8E8E93),
    showLeftStripe: Boolean = false,
    stripeColor: Color = Color.Transparent,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(40.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(40.dp))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showLeftStripe) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(6.dp)
                        .background(stripeColor)
                )
            }
            
            Row(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono principal
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(mainIconBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = mainIcon,
                        contentDescription = null,
                        tint = mainIconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Info Central
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = startTime,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = " - $endTime",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF8E8E93)
                        )
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = locationIcon,
                            contentDescription = null,
                            tint = Color(0xFF8E8E93),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = location,
                            fontSize = 14.sp,
                            color = Color(0xFF8E8E93)
                        )
                    }
                    
                    if (extraBadgeText != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(extraBadgeColor)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = extraBadgeText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = extraBadgeTextColor
                            )
                        }
                    }
                }
                
                // Status Badge
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(statusColor)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (status == "En curso") {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black)
                            )
                        }
                        Text(
                            text = status,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusTextColor
                        )
                    }
                }
            }
        }
    }
}

/*
Cards de Documentos
 */

@Composable
fun DocumentCard(
    name: String,
    date: String,
    size: String,
    onViewClick: () -> Unit,
    onDownloadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(40.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(40.dp))
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono Documento
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF1F1F1)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = Color(0xFF8E8E93),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info Central
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1
                )
                Text(
                    text = "$date • $size",
                    fontSize = 14.sp,
                    color = Color(0xFF8E8E93)
                )
            }

            // Acciones
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón Ver
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F1F1))
                        .clickable { onViewClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Ver",
                        tint = Color(0xFF8E8E93),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Botón Descargar
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF007AFF).copy(alpha = 0.1f))
                        .clickable { onDownloadClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Descargar",
                        tint = Color(0xFF007AFF),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
