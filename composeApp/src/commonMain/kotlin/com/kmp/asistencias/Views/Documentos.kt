package com.kmp.asistencias.Views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kmp.asistencias.Components.DocumentCard
import com.kmp.asistencias.Models.DocumentoUsuario
import com.kmp.asistencias.Network.Documentos as DocumentosApi

import com.kmp.asistencias.Utils.NetworkMonitor
import kotlinx.coroutines.launch

@Composable
fun Documentos() {
    val uriHandler = LocalUriHandler.current
    var documentos by remember { mutableStateOf<List<DocumentoUsuario>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    fun cargarDatos() {
        scope.launch {
            try {
                isLoading = true
                val response = DocumentosApi.GetListaDomcumentos()
                documentos = response.data
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        cargarDatos()
        
        // Recargar si vuelve el internet y no hay datos
        scope.launch {
            NetworkMonitor.isOnline.collect { online ->
                if (online && documentos.isEmpty()) {
                    try {
                        println("Documentos: Red recuperada, reintentando carga...")
                        cargarDatos()
                    } catch (e: Exception) {
                        println("Error al recargar documentos: ${e.message}")
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (documentos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay documentos disponibles",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        } else {
            documentos.forEach { doc ->
                DocumentCard(
                    name = doc.nombreArchivo,
                    date = doc.tipoDocumento,
                    size = doc.extension,
                    onViewClick = { 
                        if (doc.urlArchivo.isNotEmpty()) {
                            uriHandler.openUri(doc.urlArchivo)
                        }
                    },
                    onDownloadClick = { 
                        if (doc.urlArchivo.isNotEmpty()) {
                            uriHandler.openUri(doc.urlArchivo)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
