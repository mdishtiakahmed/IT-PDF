package com.itpdf.app.ui.screens

import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itpdf.app.domain.GeminiHelper // নিশ্চিত করুন এই ইমপোর্টটি ঠিক আছে
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiTextScreen(onBack: () -> Unit) {
    // স্টেট ভেরিয়েবল (State Variables)
    var prompt by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Assistant") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E1E1E),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            // ১. ইনপুট বক্স
            OutlinedTextField(
                value = prompt,
                onValueChange = { prompt = it },
                label = { Text("Ask anything (CV, Email, Letters)...") },
                placeholder = { Text("Example: Write a cover letter for IT job") },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                maxLines = 10
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ২. জেনারেট বাটন (Gemini কল করা হচ্ছে)
            Button(
                onClick = {
                    if (prompt.isNotEmpty()) {
                        isLoading = true
                        // কীবোর্ড হাইড করার জন্য
                        // (ঐচ্ছিক, কোড জটিল না করার জন্য বাদ দেওয়া হলো)
                        
                        scope.launch {
                            // GeminiHelper কল করা হচ্ছে
                            result = GeminiHelper.generateContent(prompt)
                            isLoading = false
                        }
                    } else {
                        Toast.makeText(context, "Please write something first!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Thinking...")
                } else {
                    Icon(Icons.Default.AutoAwesome, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generate with AI")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ৩. রেজাল্ট দেখানো (যদি রেজাল্ট থাকে)
            if (result.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Answer:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            
                            // কপি বাটন
                            IconButton(onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = android.content.ClipData.newPlainText("AI Result", result)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(Icons.Default.ContentCopy, "Copy")
                            }
                        }
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // আসল উত্তর
                        Text(result, fontSize = 16.sp, lineHeight = 24.sp)
                    }
                }
            }
        }
    }
}
