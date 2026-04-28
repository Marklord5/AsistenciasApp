package com.kmp.asistencias.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kmp.asistencias.Components.CustomTextField
import com.kmp.asistencias.Components.PrimaryButton
import com.kmp.asistencias.Network.LoginApi
import com.kmp.asistencias.Themes.BackgroundWhite
import com.kmp.asistencias.Themes.BlueCyan
import com.kmp.asistencias.Themes.PrimaryGradient
import com.kmp.asistencias.Themes.White
import com.russhwolf.settings.Settings
import kotlinx.coroutines.launch

@Composable
fun Login(onLoginSuccess: () -> Unit) {
    val focusManager = LocalFocusManager.current
    val settings = remember { Settings() }
    
    var email by remember { mutableStateOf(settings.getString("email", "")) }
    var password by remember { mutableStateOf(settings.getString("pass", "")) }
    var rememberMe by remember { mutableStateOf(settings.getBoolean("rem", false)) }
    
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
            .statusBarsPadding()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
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
                .background(PrimaryGradient) 
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = BlueCyan
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Asistencias",
                    color = White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

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
                                // Guardar o borrar según el switch de recordar
                                if (rememberMe) {
                                    settings.putString("email", email)
                                    settings.putString("pass", password)
                                    settings.putBoolean("rem", true)
                                } else {
                                    settings.remove("email")
                                    settings.remove("pass")
                                    settings.putBoolean("rem", false)
                                }
                                onLoginSuccess()
                            } else {
                                errorMessage = response.message
                            }
                        } catch (e: Exception) {
                            errorMessage = "Error de conexión: ${e.message} "
                        } finally {
                            isLoading = false
                        }
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

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

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "TSDN v1.0.0 © 2026",
            fontSize = 13.sp,
            color = Color(0xFFC7C7CC),
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}
