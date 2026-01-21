package com.itpdf.app.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.itpdf.app.domain.*
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(onBack: () -> Unit) {
    var elements by remember { mutableStateOf(listOf<EditorElement>()) }
    var selectedId by remember { mutableStateOf<Long?>(null) }
    var pageSettings by remember { mutableStateOf(PageSettings()) }
    
    val context = LocalContext.current
    valscope = rememberCoroutineScope()
    var isGenerating by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val newImage = ImageElement(
                id = System.currentTimeMillis(),
                x = 50f,
                y = 50f,
                uri = it.toString()
            )
            elements = elements + newImage
            selectedId = newImage.id
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editor") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") } },
                actions = {
                    IconButton(onClick = {
                        valscope.launch {
                            isGenerating = true
                            val file = PdfGenerator.createPdf(context, "IT_PDF_${System.currentTimeMillis()}", elements, pageSettings)
                            isGenerating = false
                            Toast.makeText(context, if(file != null) "Saved to Documents/IT_PDF" else "Error saving", Toast.LENGTH_LONG).show()
                        }
                    }) {
                        if (isGenerating) CircularProgressIndicator(modifier = Modifier.size(20.dp)) else Icon(Icons.Default.Save, "Save")
                    }
                }
            )
        },
        bottomBar = {
            Column {
                if (selectedId != null) {
                    EditorPropertiesBar(
                        element = elements.find { it.id == selectedId },
                        onUpdate = { updated -> 
                            elements = elements.map { if (it.id == updated.id) updated else it }
                        },
                        onDelete = {
                            elements = elements.filter { it.id != selectedId }
                            selectedId = null
                        }
                    )
                }
                BottomAppBar {
                    IconButton(onClick = { 
                        val newText = TextElement(System.currentTimeMillis(), 100f, 100f, text = "New Text")
                        elements = elements + newText
                        selectedId = newText.id
                    }) { Icon(Icons.Default.TextFields, "Add Text") }
                    
                    IconButton(onClick = { imagePickerLauncher.launch("image/*") }) { Icon(Icons.Default.Image, "Add Image") }
                    
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { selectedId = null }) { Icon(Icons.Default.Deselect, "Deselect") }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFEEEEEE))
                .clickable { selectedId = null } // Deselect when clicking empty space
        ) {
            // A4 Paper Representation
            Box(
                modifier = Modifier
                    .width(350.dp) // Visual scaling for A4
                    .height(500.dp)
                    .background(Color(pageSettings.backgroundColor))
                    .align(Alignment.Center)
                    .border(1.dp, Color.Gray)
            ) {
                elements.sortedBy { it.zIndex }.forEach { element ->
                    when (element) {
                        is TextElement -> DraggableTextElement(
                            element = element,
                            isSelected = element.id == selectedId,
                            onSelect = { selectedId = it },
                            onUpdate = { updated -> 
                                elements = elements.map { if (it.id == updated.id) updated else it } 
                            }
                        )
                        is ImageElement -> DraggableImageElement(
                            element = element,
                            isSelected = element.id == selectedId,
                            onSelect = { selectedId = it },
                            onUpdate = { updated ->
                                elements = elements.map { if (it.id == updated.id) updated else it }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EditorPropertiesBar(
    element: EditorElement?,
    onUpdate: (EditorElement) -> Unit,
    onDelete: () -> Unit
) {
    Surface(tonalElevation = 8.dp, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(8.dp).horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Delete", tint = Color.Red) }
            
            when (element) {
                is TextElement -> {
                    IconButton(onClick = { onUpdate(element.copy(fontSize = element.fontSize + 2)) }) { Icon(Icons.Default.Add, "Inc Size") }
                    Text("${element.fontSize.toInt()}")
                    IconButton(onClick = { onUpdate(element.copy(fontSize = (element.fontSize - 2).coerceAtLeast(8f))) }) { Icon(Icons.Default.Remove, "Dec Size") }
                    
                    IconButton(onClick = { onUpdate(element.copy(isBold = !element.isBold)) }) { 
                        Icon(Icons.Default.FormatBold, "Bold", tint = if (element.isBold) MaterialTheme.colorScheme.primary else Color.Gray) 
                    }
                    IconButton(onClick = { onUpdate(element.copy(color = if(element.color == Color.Black.toArgb().toLong()) Color.Red.toArgb().toLong() else Color.Black.toArgb().toLong())) }) {
                        Icon(Icons.Default.ColorLens, "Color")
                    }
                }
                is ImageElement -> {
                    IconButton(onClick = { onUpdate(element.copy(rotation = element.rotation + 90f)) }) { Icon(Icons.Default.RotateRight, "Rotate") }
                    IconButton(onClick = { onUpdate(element.copy(width = element.width * 1.1f, height = element.height * 1.1f)) }) { Icon(Icons.Default.ZoomIn, "Zoom In") }
                    IconButton(onClick = { onUpdate(element.copy(width = element.width * 0.9f, height = element.height * 0.9f)) }) { Icon(Icons.Default.ZoomOut, "Zoom Out") }
                }
                null -> {}
            }
        }
    }
}

@Composable
fun DraggableTextElement(
    element: TextElement,
    isSelected: Boolean,
    onSelect: (Long) -> Unit,
    onUpdate: (TextElement) -> Unit
) {
    Box(
        modifier = Modifier
            .offset { IntOffset(element.x.roundToInt(), element.y.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onSelect(element.id)
                    onUpdate(element.copy(x = element.x + dragAmount.x, y = element.y + dragAmount.y))
                }
            }
            .clickable { onSelect(element.id) }
            .border(if (isSelected) 1.dp else 0.dp, if (isSelected) Color.Blue else Color.Transparent)
            .padding(4.dp)
    ) {
        Text(
            text = element.text,
            color = Color(element.color),
            fontSize = element.fontSize.sp,
            fontWeight = if (element.isBold) FontWeight.Bold else FontWeight.Normal,
            fontStyle = if (element.isItalic) FontStyle.Italic else FontStyle.Normal
        )
    }
}

@Composable
fun DraggableImageElement(
    element: ImageElement,
    isSelected: Boolean,
    onSelect: (Long) -> Unit,
    onUpdate: (ImageElement) -> Unit
) {
    Box(
        modifier = Modifier
            .offset { IntOffset(element.x.roundToInt(), element.y.roundToInt()) }
            .size(element.width.dp, element.height.dp)
            .rotate(element.rotation)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onSelect(element.id)
                    onUpdate(element.copy(x = element.x + dragAmount.x, y = element.y + dragAmount.y))
                }
            }
            .clickable { onSelect(element.id) }
            .border(if (isSelected) 2.dp else 0.dp, if (isSelected) Color.Blue else Color.Transparent)
    ) {
        AsyncImage(
            model = element.uri,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
    }
}