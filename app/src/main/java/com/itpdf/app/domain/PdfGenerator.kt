package com.itpdf.app.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object PdfGenerator {

    fun createPdf(context: Context, fileName: String, elements: List<EditorElement>, settings: PageSettings): File? {
        val document = PdfDocument()
        // Convert screen props to PDF points. A4 is 595x842.
        val pageInfo = PdfDocument.PageInfo.Builder(settings.width, settings.height, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        
        // Draw Background
        canvas.drawColor(settings.backgroundColor.toInt())

        // Sort by z-index to draw layers correctly
        elements.sortedBy { it.zIndex }.forEach { element ->
            when (element) {
                is TextElement -> drawText(canvas, element)
                is ImageElement -> drawImage(context, canvas, element)
            }
        }

        document.finishPage(page)

        val dir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "IT_PDF")
        if (!dir.exists()) dir.mkdirs()
        
        val file = File(dir, "$fileName.pdf")

        return try {
            document.writeTo(FileOutputStream(file))
            document.close()
            file
        } catch (e: IOException) {
            document.close()
            e.printStackTrace()
            null
        }
    }

    private fun drawText(canvas: Canvas, element: TextElement) {
        val textPaint = TextPaint()
        textPaint.color = element.color.toInt()
        textPaint.textSize = element.fontSize
        textPaint.isAntiAlias = true
        
        if (element.isBold) textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        if (element.isItalic) textPaint.textSkewX = -0.25f

        // Text Layout for multiline
        val staticLayout = StaticLayout.Builder.obtain(
            element.text, 
            0, 
            element.text.length, 
            textPaint, 
            500 // Max width
        ).setAlignment(Layout.Alignment.ALIGN_NORMAL).build()

        canvas.save()
        canvas.translate(element.x, element.y)
        staticLayout.draw(canvas)
        canvas.restore()
    }

    private fun drawImage(context: Context, canvas: Canvas, element: ImageElement) {
        try {
            val inputStream = context.contentResolver.openInputStream(Uri.parse(element.uri))
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (bitmap != null) {
                val scaledBitmap = Bitmap.createScaledBitmap(
                    bitmap, 
                    element.width.toInt(), 
                    element.height.toInt(), 
                    true
                )
                
                canvas.save()
                canvas.rotate(element.rotation, element.x + element.width / 2, element.y + element.height / 2)
                canvas.drawBitmap(scaledBitmap, element.x, element.y, null)
                canvas.restore()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}