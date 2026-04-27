package com.kmp.asistencias.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kmp.asistencias.Components.CustomTextField
import com.kmp.asistencias.Components.PrimaryButton
import com.kmp.asistencias.Network.LoginApi
import com.russhwolf.multiplatformsettings.Settings
import kotlinx.coroutines.launch

@Composable
fun Login(onLoginSuccess: () -> Unit) {
    val settings = remember { Settings() }
    
    // Cargamos valores iniciales de persistencia
    var email by remember { mutableStateOf(settings.getString("saved_email", "")) }
    var password by remember { mutableStateOf(settings.getString("saved_password", "")) }
    var rememberMe by remember { mutableStateOf(settings.getBoolean("remember_me", false)) }
    
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen Superior (Card con logo)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(48.dp))
                .background(Color(0xFF1A1A1A)) // Fondo oscuro mientras no hay imagen
        ) {
            // Logo GeoTime en la parte inferior izquierda de la imagen
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono circular amarillo del logo
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFAFD2E)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Email, // Placeholder icon
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "GeoTime",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Textos de Bienvenida
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Bienvenido de nuevo",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black,
                letterSpacing = (-0.5).sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Ingresa tus credenciales para registrar tu jornada.",
                fontSize = 16.sp,
                color = Color(0xFF8E8E93),
                lineHeight = 22.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Campos de Texto
        CustomTextField(
            value = email,
            onValueChange = { email = it },
            label = "Correo electrónico",
            placeholder = "correo@empresa.com",
            trailingIcon = Icons.Default.Email
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomTextField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            placeholder = "********",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Fila de Recordar y Olvidaste contraseña
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFFD1D1D6),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFE5E5EA),
                        uncheckedBorderColor = Color.Transparent
                    ),
                    modifier = Modifier.scale(0.8f)
                )
                Text(
                    text = "Recordar",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            Text(
                text = "¿Olvidaste tu contraseña?",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.clickable { /* TODO */ }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Botón Iniciar Sesión
        PrimaryButton(
            text = if (isLoading) "Cargando..." else "Iniciar Sesión",
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty() && !isLoading) {
                    scope.launch {
                        isLoading = true
                        errorMessage = null
                        try {
                            val response = LoginApi.login(email, password)
                            if (response.success) {
                                // Guardar o limpiar credenciales según 'rememberMe'
                                if (rememberMe) {
                                    settings.putString("saved_email", email)
                                    settings.putString("saved_password", password)
                                    settings.putBoolean("remember_me", true)
                                } else {
                                    settings.remove("saved_email")
                                    settings.remove("saved_password")
                                    settings.putBoolean("remember_me", false)
                                }

                                onLoginSuccess()
                            } else {
                                errorMessage = "Credenciales incorrectas"
                            }
                        } catch (e: Exception) {
                            errorMessage = "Error de conexión: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Footer: Registro
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color(0xFF8E8E93))) {
                    append("¿No tienes cuenta? ")
                }
                withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Bold)) {
                    append("Regístrate aquí")
                }
            },
            fontSize = 15.sp,
            modifier = Modifier.clickable { /* TODO */ }
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Versión y Copyright
        Text(
            text = "TSDN v1.0.0 © 2025",
            fontSize = 13.sp,
            color = Color(0xFFC7C7CC),
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}
