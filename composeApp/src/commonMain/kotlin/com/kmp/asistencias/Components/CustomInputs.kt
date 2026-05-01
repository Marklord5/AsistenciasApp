package com.kmp.asistencias.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kmp.asistencias.Themes.BlueDeep
import com.kmp.asistencias.Themes.White
import com.kmp.asistencias.Themes.GrayLight
import com.kmp.asistencias.Themes.PrimaryGradient

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    trailingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 16.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedBorderColor = BlueDeep,
                cursorColor = BlueDeep,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            trailingIcon = {
                if (isPassword) {
                    val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else if (trailingIcon != null) {
                    Icon(imageVector = trailingIcon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            singleLine = true
        )
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = Icons.AutoMirrored.Filled.ArrowForward
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(32.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PrimaryGradient),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = text.uppercase(),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    letterSpacing = 0.5.sp,
                    color = White
                )
                if (icon != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = icon, 
                        contentDescription = null, 
                        modifier = Modifier.size(24.dp),
                        tint = White
                    )
                }
            }
        }
    }
}

