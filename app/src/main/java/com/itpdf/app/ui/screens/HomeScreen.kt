package com.itpdf.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToTools: () -> Unit,
    onNavigateToEditor: () -> Unit,
    onNavigateToCv: () -> Unit,
    onNavigateToMyPdfs: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToPro: () -> Unit,
    onNavigateToAi: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("IT PDF", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                        Text("Your smart document assistant", fontSize = 12.sp, style = MaterialTheme.typography.bodySmall)
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) { Icon(Icons.Default.Settings, "Settings") }
                    IconButton(onClick = onNavigateToPro) { Icon(Icons.Default.WorkspacePremium, "Pro", tint = Color(0xFFFFD700)) }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.Home, "Home") }, label = { Text("Home") })
                NavigationBarItem(selected = false, onClick = onNavigateToTools, icon = { Icon(Icons.Default.Build, "Tools") }, label = { Text("Tools") })
                NavigationBarItem(selected = false, onClick = onNavigateToMyPdfs, icon = { Icon(Icons.Default.Folder, "Files") }, label = { Text("Files") })
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ActionCard(
                    title = "PDF Editor",
                    subtitle = "Create & Edit with Images/Text",
                    icon = Icons.Default.EditDocument,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    onClick = onNavigateToEditor
                )
            }
            item {
                ActionCard(
                    title = "AI Text Generator",
                    subtitle = "Generate Content & Letters",
                    icon = Icons.Default.AutoAwesome,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    onClick = onNavigateToAi
                )
            }
            item {
                ActionCard(
                    title = "Create CV with AI",
                    subtitle = "Write & Design Resume",
                    icon = Icons.Default.Description,
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    onClick = onNavigateToCv
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("AdMob Banner Area", color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun ActionCard(title: String, subtitle: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = Modifier.fillMaxWidth().height(100.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}