package com.itpdf.app.domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

// এই ক্লাসগুলো আপনার এডিটর এলিমেন্ট ডিফাইন করে
sealed class EditorElement {
    abstract val id: Long
    abstract val x: Float
    abstract val y: Float
    abstract val zIndex: Int
}

data class TextElement(
    override val id: Long,
    override val x: Float,
    override val y: Float,
    override val zIndex: Int = 0,
    val text: String,
    val fontSize: Float = 20f,
    val color: Long = Color.Black.toArgb().toLong(),
    val isBold: Boolean = false,
    val isItalic: Boolean = false
) : EditorElement()

data class ImageElement(
    override val id: Long,
    override val x: Float,
    override val y: Float,
    override val zIndex: Int = 0,
    val uri: String,
    val width: Float = 200f,
    val height: Float = 200f,
    val rotation: Float = 0f
) : EditorElement()

data class PageSettings(
    val backgroundColor: Long = Color.White.toArgb().toLong(),
    val pageWidth: Int = 595, // A4 সাইজ (Width)
    val pageHeight: Int = 842 // A4 সাইজ (Height)
)
