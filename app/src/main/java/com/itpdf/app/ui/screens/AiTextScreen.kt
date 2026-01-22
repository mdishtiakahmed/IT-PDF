package com.itpdf.app.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiTextScreen(onBack: () -> Unit) {
    var prompt by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // আপনার দেওয়া API Key টি এখানে বসানো হয়েছে
    val apiKey = "AIzaSyAg8971YAPbeIOjMbkZj5-lv81PD-H8pyE"

    // মডেল ইনিশিয়ালাইজ করা
    val generativeModel = remember {
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = apiKey
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Writer") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("What should I write for you?", style = MaterialTheme.typography.titleMedium)
            
            OutlinedTextField(
                value = prompt,
                onValueChange = { prompt = it },
                label = { Text("Enter topic (e.g., Leave application, Cover letter)") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                maxLines = 5
            )

            Button(
                onClick = {
                    if (prompt.isNotBlank()) {
                        isLoading = true
                        scope.launch {
                            try {
                                // সরাসরি API কল করা হচ্ছে
                                val response = generativeModel.generateContent(prompt)
                                result = response.text ?: "No response found."
                                isLoading = false
                            } catch (e: Exception) {
                                isLoading = false
                                result = ""
                                Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Please write something first", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generating...")
                } else {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generate")
                }
            }

            if (result.isNotEmpty()) {
                HorizontalDivider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Result:", style = MaterialTheme.typography.titleMedium)
                    IconButton(onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("AI Result", result)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.ContentCopy, "Copy")
                    }
                }
                
                Card(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
                        Text(result)
                    }
                }
            }
        }
    }
}
