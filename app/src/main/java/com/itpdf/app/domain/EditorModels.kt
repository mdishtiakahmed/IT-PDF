package com.itpdf.app.domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

sealed interface EditorElement {
    val id: Long
    var x: Float
    var y: Float
    var zIndex: Float
}

data class TextElement(
    override val id: Long,
    override var x: Float,
    override var y: Float,
    override var zIndex: Float = 0f,
    var text: String,
    var fontSize: Float = 20f,
    var color: Long = Color.Black.toArgb().toLong(),
    var isBold: Boolean = false,
    var isItalic: Boolean = false
) : EditorElement

data class ImageElement(
    override val id: Long,
    override var x: Float,
    override var y: Float,
    override var zIndex: Float = 0f,
    var uri: String,
    var width: Float = 200f,
    var height: Float = 200f,
    var rotation: Float = 0f
) : EditorElement

data class PageSettings(
    val width: Int = 595, // A4 width in points
    val height: Int = 842, // A4 height in points
    val backgroundColor: Long = Color.White.toArgb().toLong()
)