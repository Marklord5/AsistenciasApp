package com.kmp.asistencias.Views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kmp.asistencias.components.DocumentCard

@Composable
fun Documentos() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {


        val documentos = listOf(
            DocumentoItem("Boleta de Pago - Octubre 2023", "15 Oct 2023", "1.2 MB"),
            DocumentoItem("Contrato de Trabajo.pdf", "01 Sep 2023", "4.5 MB"),
            DocumentoItem("Certificado Laboral.pdf", "20 Ago 2023", "850 KB"),
            DocumentoItem("Reglamento Interno.pdf", "15 Ene 2023", "12.3 MB"),
            DocumentoItem("Manual de Convivencia.pdf", "10 Ene 2023", "2.1 MB"),
            DocumentoItem("Seguro Vida Ley.pdf", "05 Ene 2023", "1.1 MB")
        )

        documentos.forEach { doc ->
            DocumentCard(
                name = doc.nombre,
                date = doc.fecha,
                size = doc.peso,
                onViewClick = { /* Acción para visualizar */ },
                onDownloadClick = { /* Acción para descargar */ }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

data class DocumentoItem(
    val nombre: String,
    val fecha: String,
    val peso: String
)
