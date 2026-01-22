package com.itpdf.app.ui.screens

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPdfsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var files by remember { mutableStateOf(listOf<File>()) }

    // ফাইল লোড করার লজিক (Public Documents ফোল্ডার থেকে)
    LaunchedEffect(Unit) {
        files = loadFilesFromStorage()
    }

    // ফাইল ওপেন ফাংশন
    fun openFile(file: File) {
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NO_HISTORY
            }
            // ব্যবহারকারীকে অ্যাপ চুজ করতে দিন
            val chooser = Intent.createChooser(intent, "Open PDF with")
            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(context, "No PDF viewer found", Toast.LENGTH_SHORT).show()
        }
    }

    // ফাইল শেয়ার ফাংশন
    fun shareFile(file: File) {
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            context.startActivity(Intent.createChooser(intent, "Share PDF"))
        } catch (e: Exception) {
            Toast.makeText(context, "Error sharing file", Toast.LENGTH_SHORT).show()
        }
    }

    // ফাইল ডিলিট ফাংশন
    fun deleteFile(file: File) {
        try {
            if (file.delete()) {
                files = loadFilesFromStorage() // লিস্ট রিফ্রেশ
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error deleting file", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Files") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        if (files.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No PDFs found in Documents/IT_PDF", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(files) { file ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { openFile(file) },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.PictureAsPdf, null, tint = Color.Red, modifier = Modifier.size(40.dp))
                            Spacer(Modifier.width(16.dp))
                            Column(Modifier.weight(1f)) {
                                Text(file.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("${file.length() / 1024} KB", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                            IconButton(onClick = { shareFile(file) }) { Icon(Icons.Default.Share, "Share", tint = Color.Gray) }
                            IconButton(onClick = { deleteFile(file) }) { Icon(Icons.Default.Delete, "Delete", tint = Color.Red) }
                        }
                    }
                }
            }
        }
    }
}

// আলাদা ফাংশন যাতে কোড ক্লিন থাকে
fun loadFilesFromStorage(): List<File> {
    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
    val appDir = File(downloadsDir, "IT_PDF")
    
    // ফোল্ডার না থাকলে তৈরি করুন (সেভ করার সময় কাজে লাগবে)
    if (!appDir.exists()) appDir.mkdirs()

    return appDir.listFiles()
        ?.filter { it.extension.equals("pdf", ignoreCase = true) }
        ?.sortedByDescending { it.lastModified() } 
        ?: emptyList()
}
