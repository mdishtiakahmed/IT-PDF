package com.itpdf.app.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFilesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var fileList by remember { mutableStateOf(listOf<File>()) }

    // ফাইল লোড করার ফাংশন
    fun loadFiles() {
        // আমরা Documents/IT_PDF অথবা অ্যাপের নিজস্ব ফোল্ডার থেকে ফাইল খুঁজবো
        val dir = File(context.getExternalFilesDir(null), "IT_PDF") // অথবা Environment.DIRECTORY_DOCUMENTS
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val files = dir.listFiles()?.filter { it.extension.equals("pdf", ignoreCase = true) }?.sortedByDescending { it.lastModified() }
        fileList = files ?: emptyList()
    }

    // স্ক্রিন চালু হলেই ফাইল লোড হবে
    LaunchedEffect(Unit) {
        loadFiles()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Files") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        if (fileList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No PDF files found", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(fileList) { file ->
                    FileItem(
                        file = file,
                        onClick = { openPdf(context, file) },
                        onShare = { sharePdf(context, file) },
                        onDelete = {
                            if (file.delete()) {
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                                loadFiles() // লিস্ট রিফ্রেশ
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FileItem(file: File, onClick: () -> Unit, onShare: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Description, contentDescription = null, tint = Color.Red, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(file.name, fontWeight = FontWeight.Bold, maxLines = 1)
                Text("${file.length() / 1024} KB", fontSize = 12.sp, color = Color.Gray)
            }
            IconButton(onClick = onShare) { Icon(Icons.Default.Share, "Share") }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Delete", tint = Color.Gray) }
        }
    }
}

// পিডিএফ ওপেন করার লজিক (FileProvider ব্যবহার করে)
fun openPdf(context: Context, file: File) {
    try {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        context.startActivity(Intent.createChooser(intent, "Open PDF"))
    } catch (e: Exception) {
        Toast.makeText(context, "No PDF Viewer found", Toast.LENGTH_SHORT).show()
    }
}

fun sharePdf(context: Context, file: File) {
    try {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "application/pdf"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        context.startActivity(Intent.createChooser(intent, "Share PDF"))
    } catch (e: Exception) {
        Toast.makeText(context, "Error sharing", Toast.LENGTH_SHORT).show()
    }
}
