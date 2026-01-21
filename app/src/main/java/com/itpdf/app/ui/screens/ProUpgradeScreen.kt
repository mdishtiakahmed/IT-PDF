package com.itpdf.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProUpgradeScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Go Pro") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = Color(0xFFFFD700)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Upgrade to IT PDF Pro",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Unlock the full power of AI and PDF tools",
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            
            Spacer(Modifier.height(32.dp))
            
            ProFeatureItem("Remove All Ads")
            ProFeatureItem("Unlimited AI Generations")
            ProFeatureItem("Premium CV Templates")
            ProFeatureItem("Priority Support")
            
            Spacer(Modifier.weight(1f))
            
            Button(
                onClick = { /* Launch Billing Flow */ },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700), contentColor = Color.Black)
            ) {
                Text("Upgrade Now - $4.99", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProFeatureItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(16.dp))
        Text(text, fontSize = 16.sp)
    }
}