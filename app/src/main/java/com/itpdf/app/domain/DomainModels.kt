package com.itpdf.app.domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

// ১. এডিটর এলিমেন্টের প্যারেন্ট ক্লাস
sealed class EditorElement {
    abstract val id: Long
    abstract val x: Float
    abstract val y: Float
    abstract val zIndex: Int
}

// ২. টেক্সট এলিমেন্ট
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

// ৩. ইমেজ এলিমেন্ট
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

// ৪. পেজ সেটিংস (A4 সাইজ ডিফল্ট)
data class PageSettings(
    val backgroundColor: Long = Color.White.toArgb().toLong(),
    val pageWidth: Int = 595, 
    val pageHeight: Int = 842 
)
