package com.itpdf.app.domain

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object PdfGenerator {

    // Suspend ফাংশন ব্যবহার করা হয়েছে যাতে UI আটকে না যায়
    suspend fun createPdf(
        context: Context, 
        fileName: String, 
        elements: List<EditorElement>, 
        settings: PageSettings
    ): File? {
        return withContext(Dispatchers.IO) {
            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(settings.pageWidth, settings.pageHeight, 1).create()
            val page = document.startPage(pageInfo)
            val canvas = page.canvas
            
            // ব্যাকগ্রাউন্ড আঁকা
            canvas.drawColor(settings.backgroundColor.toInt())

            // লেয়ার অনুযায়ী সাজিয়ে ড্র করা
            elements.sortedBy { it.zIndex }.forEach { element ->
                when (element) {
                    is TextElement -> drawText(canvas, element)
                    is ImageElement -> drawImage(context, canvas, element)
                }
            }

            document.finishPage(page)

            // ফাইল সেভ করা (পাবলিক ফোল্ডারে)
            val savedFile = savePdfToPublicFolder(context, document, fileName)
            
            document.close()
            savedFile
        }
    }

    private fun drawText(canvas: Canvas, element: TextElement) {
        val textPaint = TextPaint()
        textPaint.color = element.color.toInt()
        textPaint.textSize = element.fontSize
        textPaint.isAntiAlias = true
        
        if (element.isBold) textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        if (element.isItalic) textPaint.textSkewX = -0.25f

        // আপনার দেওয়া StaticLayout লজিক (মাল্টিলাইনের জন্য সেরা)
        val builder = StaticLayout.Builder.obtain(
            element.text, 
            0, 
            element.text.length, 
            textPaint, 
            500 // ম্যাক্স উইডথ
        ).setAlignment(Layout.Alignment.ALIGN_NORMAL)
        
        val staticLayout = builder.build()

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
                // রোটেশন লজিক
                canvas.rotate(element.rotation, element.x + element.width / 2, element.y + element.height / 2)
                canvas.drawBitmap(scaledBitmap, element.x, element.y, null)
                canvas.restore()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // ফাইল সেভ করার অ্যাডভান্সড ফাংশন (Android 10+ সাপোর্ট সহ)
    private fun savePdfToPublicFolder(context: Context, document: PdfDocument, fileName: String): File? {
        val finalName = "$fileName.pdf"
        
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ (পাবলিক Documents ফোল্ডারে সেভ হবে)
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, finalName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/IT_PDF")
                }
                
                val uri = context.contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
                uri?.let {
                    context.contentResolver.openOutputStream(it)?.use { stream ->
                        document.writeTo(stream)
                    }
                    // রিটার্ন করার জন্য একটি ফেইক পাথ (URI দিয়ে কাজ হয়ে গেছে)
                    return File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "IT_PDF/$finalName")
                }
            } else {
                // Android 9 বা তার নিচের ভার্সন
                val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "IT_PDF")
                if (!dir.exists()) dir.mkdirs()
                
                val file = File(dir, finalName)
                FileOutputStream(file).use {
                    document.writeTo(it)
                }
                return file
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
