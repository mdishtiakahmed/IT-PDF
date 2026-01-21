package com.itpdf.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.itpdf.app.data.GeminiService
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CvBuilderScreen(onBack: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var jobTitle by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    
    val scope = rememberCoroutineScope()
    var isAiLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CV Builder") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Personal Details", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = jobTitle,
                onValueChange = { jobTitle = it },
                label = { Text("Job Title (e.g. Android Dev)") },
                modifier = Modifier.fillMaxWidth()
            )

            Divider()

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Professional Summary", style = MaterialTheme.typography.titleLarge)
                TextButton(onClick = {
                    if (jobTitle.isNotEmpty()) {
                        scope.launch {
                            isAiLoading = true
                            val result = GeminiService.generateCvSection(jobTitle, "Professional Summary")
                            result.onSuccess { summary = it }
                            isAiLoading = false
                        }
                    }
                }) {
                    if (isAiLoading) CircularProgressIndicator(modifier = Modifier.size(16.dp)) else Icon(Icons.Default.AutoAwesome, "AI Generate")
                }
            }
            OutlinedTextField(
                value = summary,
                onValueChange = { summary = it },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                placeholder = { Text("Write about yourself...") }
            )

            Button(
                onClick = { /* Generate PDF Logic */ },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Generate CV")
            }
        }
    }
}