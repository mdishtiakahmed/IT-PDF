package com.itpdf.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

data class Tool(val name: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val tools = listOf(
        Tool("Merge PDF", Icons.Default.Merge),
        Tool("Split PDF", Icons.Default.CallSplit),
        Tool("Compress", Icons.Default.Compress),
        Tool("Text to PDF", Icons.Default.TextFields),
        Tool("Lock PDF", Icons.Default.Lock),
        Tool("Unlock PDF", Icons.Default.LockOpen),
        Tool("Rotate", Icons.Default.RotateRight),
        Tool("Web to PDF", Icons.Default.Web)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tools") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(padding)
        ) {
            items(tools) { tool ->
                ToolCard(tool) {
                    // এখানে পরে ফাংশন বসানো হবে, আপাতত টোস্ট দেখানো হচ্ছে
                    Toast.makeText(context, "${tool.name} coming soon!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
fun ToolCard(tool: Tool, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .height(100.dp)
            .clickable { onClick() }, // ক্লিক অপশন যোগ করা হয়েছে
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(tool.icon, contentDescription = null, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(tool.name)
        }
    }
}
